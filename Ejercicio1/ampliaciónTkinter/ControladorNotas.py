import tkinter as tk
from tkinter import messagebox  # Para mostrar mensajes de confirmación
from threading import Thread
import requests
from io import BytesIO
from PIL import Image

class ControladorNotas:
    def __init__(self, vista, modelo):
        # Inicializa el controlador y asigna eventos de botones
        self.vista = vista
        self.modelo = modelo

        # Asignar funciones a los botones
        self.vista.boton_agregar.config(command=self.agregar_nota)
        self.vista.boton_eliminar.config(command=self.eliminar_nota)
        self.vista.boton_guardar.config(command=self.guardar_notas)
        self.vista.boton_cargar.config(command=self.cargar_notas)
        self.vista.boton_descargar.config(command=self.descargar_imagen)

        # Cargar notas al inicio
        self.cargar_notas()

    def agregar_nota(self):
        # Agrega una nueva nota a la lista
        nueva_nota = self.vista.entry_nota.get()
        if nueva_nota:
            self.modelo.agregar_nota(nueva_nota)
            self.actualizar_listbox()
            self.vista.entry_nota.delete(0, tk.END)

    def eliminar_nota(self):
        # Elimina la nota seleccionada en el Listbox
        indice_seleccionado = self.vista.listbox.curselection()
        if indice_seleccionado:
            self.modelo.eliminar_nota(indice_seleccionado[0])
            self.actualizar_listbox()
        else:
            messagebox.showwarning("Advertencia", "Seleccione una nota para eliminar.")

    def guardar_notas(self):
        # Guarda todas las notas en un archivo
        self.modelo.guardar_notas()
        messagebox.showinfo("Información", "Notas guardadas correctamente.")

    def cargar_notas(self):
        # Carga las notas desde el archivo y las muestra en el Listbox
        self.modelo.cargar_notas()
        self.actualizar_listbox()

    def descargar_imagen(self):
        # Inicia la descarga de una imagen desde una URL en un hilo separado
        url = "https://th.bing.com/th/id/R.73e5c6dd983f9f2839ef11108d93a78a?rik=rS5J1%2fa1kvheUw&riu=http%3a%2f%2fimages4.fanpop.com%2fimage%2fphotos%2f21000000%2fjousuke-tsunami-inazuma-eleven-21029784-312-176.jpg&ehk=4LNeKFbaX%2fbm2nFPtG2HPiW8r%2bB8tQQA8%2bYK4N6wD9g%3d&risl=&pid=ImgRaw&r=0"
        thread = Thread(target=self._descargar_imagen_thread, args=(url,))
        thread.start()

    def _descargar_imagen_thread(self, url):
        # Realiza la descarga de la imagen y la muestra en la interfaz
        try:
            response = requests.get(url)
            img_data = response.content
            image = Image.open(BytesIO(img_data))
            self.vista.mostrar_imagen(image)
        except Exception as e:
            print(f"Error al descargar la imagen: {e}")

    def actualizar_listbox(self):
        # Actualiza el Listbox con las notas actuales
        self.vista.listbox.delete(0, tk.END)
        for nota in self.modelo.obtener_notas():
            self.vista.listbox.insert(tk.END, nota)
