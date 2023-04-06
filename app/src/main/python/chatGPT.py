import http.client
import re
import openai


def get_daily_tip():
    #Control api_key is valid :)
    openai.api_key = ""

    conn = http.client.HTTPSConnection("opendata.aemet.es")

    cordoba_code = 14

    api_key = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqbWF0ZW9zYmFycm9zb0BnbWFpbC5jb20iLCJqdGkiOiI2Yzk2NGU3Yy1hNjllLTQyZGMtOGFmYi05MGRmNDdkYzUxMTYiLCJpc3MiOiJBRU1FVCIsImlhdCI6MTY4MDYwOTY5MSwidXNlcklkIjoiNmM5NjRlN2MtYTY5ZS00MmRjLThhZmItOTBkZjQ3ZGM1MTE2Iiwicm9sZSI6IiJ9.SFySasaFPqjZTTK7jAV3UkjLpivQ4L2gPYp7vHToheI"

    conn.request("GET", f"/opendata/api/prediccion/provincia/hoy/{cordoba_code}?api_key={api_key}")

    res = conn.getresponse()
    data = res.read()

    conn.request("GET", eval(data.decode("utf-8"))["datos"])
    res = conn.getresponse()

    texto = res.read()

    patron = re.compile(b'\nC\xd3RDOBA(.*)TEMPERATURAS', re.DOTALL)
    resultado = patron.search(texto)
    data = resultado.group(1)

    texto = data.decode('ISO-8859-1')

    #print(texto)

    prompt = "Tengo ojos secos, sufren especialmente en ambientes húmedos y con muchas partículas en el aire. Quiero que me respondas únicamente con recomendaciones sobre con qué debería tener cuidado especial hoy para proteger mis ojos. A continuación te dejo un resumen del estado de Córdoba hoy:  Clima hoy: \"{texto} \" "

    completion = openai.Completion.create(engine="text-davinci-003",
                                          prompt=prompt,
                                          max_tokens=1000)
    return [" ".join(texto.split()), " ".join(completion.choices[0].text.split())].copy()

if __name__ == "__main__":
    res = get_daily_tip()
    print(res[0])
    print()
    print(res[1])