import requests
from PIL import Image, ImageTk
import io


def descargar_imagen(url, size=(150, 150)):
    try:
        response = requests.get(url)
        response.raise_for_status()
        image = Image.open(io.BytesIO(response.content))
        image = image.resize(size, Image.LANCZOS)
        return ImageTk.PhotoImage(image)

    except requests.RequestException as e:
        print(f"Error al descargar la imagen de {url}: {e}")
        return None
