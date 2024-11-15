import requests
from io import BytesIO
from PIL import Image


def descargar_imagen(url, callback):
    """Descargar la imagen desde una URL y llamar al callback con la imagen cargada"""
    try:
        # Descargar la imagen desde la URL
        res = requests.get(url)
        res.raise_for_status()  # Lanza una excepción si la descarga falla
        imagen = Image.open(BytesIO(res.content))  # Cargar la imagen con PIL
        callback(imagen)  # Llamar al callback pasando la imagen descargada
    except requests.exceptions.RequestException as e:
        print(f"Error al descargar la imagen: {e}")
        callback(None)  # Llamar al callback con None en caso de error
