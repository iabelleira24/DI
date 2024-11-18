import tkinter as tk
from tkinter import simpledialog, messagebox
from PIL import Image, ImageTk
import requests
from io import BytesIO


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


class App:
    def __init__(self, root):
        self.root = root
        self.root.title("Descargador de Imágenes")

        # Crear el marco principal
        self.frame = tk.Frame(self.root)
        self.frame.pack(padx=20, pady=20)

        # Campo de entrada para la URL
        self.url_label = tk.Label(self.frame, text="Ingrese la URL de la imagen:")
        self.url_label.pack(pady=5)

        self.url_entry = tk.Entry(self.frame, width=50)
        self.url_entry.pack(pady=5)

        # Botón para descargar la imagen
        self.download_button = tk.Button(self.frame, text="Descargar", command=self.descargar_y_mostrar)
        self.download_button.pack(pady=10)

        # Área para mostrar la imagen descargada
        self.image_label = tk.Label(self.frame, text="La imagen aparecerá aquí.", width=40, height=20, bg="lightgray")
        self.image_label.pack(pady=10)

        # Almacenar la imagen descargada
        self.image = None

    def descargar_y_mostrar(self):
        """Descargar la imagen desde la URL y mostrarla en la interfaz"""
        url = self.url_entry.get().strip()
        if not url:
            messagebox.showerror("Error", "Por favor, ingrese una URL.")
            return

        # Llamar a la función de descarga
        descargar_imagen(url, self.mostrar_imagen)

    def mostrar_imagen(self, imagen):
        """Mostrar la imagen descargada en la interfaz"""
        if imagen is None:
            messagebox.showerror("Error", "No se pudo descargar la imagen. Verifique la URL.")
            return

        # Redimensionar la imagen para que quepa en el área de visualización
        imagen = imagen.resize((300, 300), Image.ANTIALIAS)
        self.image = ImageTk.PhotoImage(imagen)

        # Mostrar la imagen en el Label
        self.image_label.config(image=self.image, text="")