import pandas
from dotenv import load_dotenv
load_dotenv()
import os
import pyodbc, struct
from azure import identity

caminho_csv = os.environ["CSV_LIVROS"]
df_livros = pandas.read_csv(caminho_csv, sep=";")

df_livros = df_livros.drop(columns=["edicao","editora"])
df_livros = df_livros.drop_duplicates()

# Tratamento do isbn

df_livros = df_livros.dropna(subset = ["isbn"])

#Extrai somente o isbn, removendo o que tem a mais na coluna
df_livros["isbn"] = df_livros["isbn"].astype("str").apply(lambda texto: texto.split("(")[0])
df_livros["isbn"] = df_livros["isbn"].str.replace(r"[^0-9X]", "", regex=True)

# Remove linhas com tamanho do isbn inválido
df_livros = df_livros.where(df_livros["isbn"].str.len()<=13)
df_livros = df_livros.drop_duplicates(subset="isbn") #Remove duplicações com mesmo isbn

# Tratamento do título

df_livros["titulo_completo"] = df_livros["titulo"].str.cat(df_livros["sub_titulo"], sep=" ") #Concatena título e subtítulo

#Onde o subtítulo é nulo, o valor ficou nulo, então o valor do título é atribuído
df_livros["titulo_completo"] = df_livros["titulo_completo"].mask(df_livros["titulo_completo"].isna(), df_livros["titulo"])

#Remove o espaço antes dos dois pontos
df_livros["titulo_completo"] = df_livros["titulo_completo"].str.replace(" :", ":")

# Tratamento do autor

df_livros = df_livros.drop_duplicates(subset=["titulo_completo", "autor", "autores_secundarios"])

#Remove livros que não tem informação de autor, nem nos secundários
df_livros = df_livros.dropna(subset=["autor","autores_secundarios"], how="all") 

# Divide os autores secundários e extrai só o primeiro
df_livros["autores_secundarios"] = df_livros["autores_secundarios"].astype("str").apply(lambda texto: texto.split('#', 1)[0]) 

# Completa com o valor do primeiro autor secundário onde o autor é nulo
df_livros["autor"] = df_livros["autor"].mask(df_livros["autor"].isna(), df_livros["autores_secundarios"])

# Os nomes estão com sobrenome primeiro. O método inverte a ordem.
def inverter_nome(nome):
    return nome.split(",")[::-1]

# Inverte os nomes para o sobrenome ficar no fim
df_livros["autor"] = df_livros["autor"].astype("str").apply(inverter_nome)
df_livros["autor"] = df_livros["autor"].str.join(" ")

# Remove espaços no início ou final
df_livros["autor"] = df_livros["autor"].str.strip()

# Tratamento do ano

# Onde o ano é nulo, é preenchido com o ano de publicação
df_livros["ano"] = df_livros["ano"].mask(df_livros["ano"].isna(), df_livros["ano_publicacao"])

# Extrai somente os valores numéricos
df_livros["ano"] = df_livros["ano"].str.extract(r'(^\d{1,4}$)')

df_livros = df_livros.dropna(subset=["ano"])
df_livros["ano"] = pandas.to_numeric(df_livros["ano"])
df_livros = df_livros.drop(columns=["ano_publicacao", "autores_secundarios", "titulo", "sub_titulo"])

# Separação dos dados dos autores

# Cria um novo dataframe com os valores únicos dos nomes dos autores
df_autores = df_livros["autor"].to_frame().drop_duplicates()

# Cria um id para cada autor a partir do index
df_autores["id_autor"] = df_autores.index.to_series().apply(lambda item: item+1)

# Junta o dataframe dos autores ao dos livros, para os livros terem o id do autor
df_livros = df_livros.merge(df_autores, how="left", on="autor", validate="m:1")

# Remove o nome do autor do dataframe de livros, para ficar só o id
df_livros = df_livros.drop(columns=["autor"])

def inserir_dados():
    # Cria a conexão com o banco de dados
    connection_string = os.environ["AZURE_SQL_CONNECTIONSTRING"]
    try:
        conn = pyodbc.connect(connection_string, timeout=10)
    except:
        conn = pyodbc.connect(connection_string, timeout=10)
    print("Conectado")
    cursor = conn.cursor()

    for index, linha in df_autores.iterrows():
        cursor.execute("INSERT INTO [dbo].[AUTOR] (id,nome) values(?,?)", linha.id_autor, linha.autor)

    for index, linha in df_livros.iterrows():
        cursor.execute("INSERT INTO [dbo].[LIVRO] (ISBN,titulo,ano,id_autor) values(?,?,?,?)", linha.isbn, linha.titulo_completo, linha.ano, linha.id_autor)

    conn.commit()
    print("Dados inseridos")
    cursor.close()
    conn.close()

inserir_dados()