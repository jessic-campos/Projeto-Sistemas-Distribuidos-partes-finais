import json
import urllib.request
import urllib.error

BASE_URL = "http://localhost:8080"


def request(method, path, data=None):
    body = None
    headers = {}
    if data is not None:
        body = json.dumps(data).encode("utf-8")
        headers["Content-Type"] = "application/json"
    req = urllib.request.Request(BASE_URL + path, data=body, headers=headers, method=method)
    try:
        with urllib.request.urlopen(req) as resp:
            return resp.read().decode("utf-8")
    except urllib.error.HTTPError as e:
        return e.read().decode("utf-8")
    except urllib.error.URLError:
        return '{"ok": false, "erro": "Servidor indisponivel. Inicie o ServidorAPI."}'


def criar_mural():
    largura = int(input("Largura: "))
    altura = int(input("Altura: "))
    dono = input("Dono: ")
    print(request("POST", "/mural", {"largura": largura, "altura": altura, "dono": dono}))


def ler_xy():
    x = int(input("x: "))
    y = int(input("y: "))
    return x, y


def pintar_pixel():
    x, y = ler_xy()
    cor = input("Cor: ")
    print(request("POST", "/pixel", {"x": x, "y": y, "cor": cor}))


def apagar_pixel():
    x, y = ler_xy()
    print(request("DELETE", f"/pixel/{x}/{y}"))


def listar_pixels():
    print(request("GET", "/pixels"))


def aplicar_pincel():
    x1 = int(input("x inicial: "))
    y1 = int(input("y inicial: "))
    x2 = int(input("x final: "))
    y2 = int(input("y final: "))
    cor = input("Cor: ")

    print(request("POST", "/pincel", {
        "x1": x1,
        "y1": y1,
        "x2": x2,
        "y2": y2,
        "cor": cor
    }))


def aplicar_borracha():
    x, y = ler_xy()
    print(request("POST", "/borracha", {"x": x, "y": y}))


def visualizar_mural():
    resposta = request("GET", "/mural?ansi=true")

    try:
        dados = json.loads(resposta)
        mural = dados.get("mural", "")

        mural = mural.replace("\\n", "\n")
        mural = mural.replace("\\r", "\r")
        mural = mural.replace("\\u001B", "\033")
        mural = mural.replace("\\u001b", "\033")

        print(mural)

    except Exception as e:
        print("Erro ao interpretar mural:")
        print(e)
        print(resposta)



def status_fila():
    print(request("GET", "/fila/status"))


def menu():
    print("\n=== Cliente Python - Pixel Art API ===")
    print("1 - Criar mural")
    print("2 - Pintar pixel")
    print("3 - Apagar pixel")
    print("4 - Listar pixels")
    print("5 - Aplicar pincel")
    print("6 - Aplicar borracha")
    print("7 - Visualizar mural")
    print("8 - Status da fila")
    print("0 - Sair")
    return input("Opcao: ")


while True:
    opcao = menu()
    if opcao == "1":
        criar_mural()
    elif opcao == "2":
        pintar_pixel()
    elif opcao == "3":
        apagar_pixel()
    elif opcao == "4":
        listar_pixels()
    elif opcao == "5":
        aplicar_pincel()
    elif opcao == "6":
        aplicar_borracha()
    elif opcao == "7":
        visualizar_mural()
    elif opcao == "8":
        status_fila()
    elif opcao == "0":
        print("Saindo...")
        break
    else:
        print("Opcao invalida")
