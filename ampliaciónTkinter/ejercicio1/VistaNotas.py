import tkinter as tk
from PIL import Image, ImageTk

class VistaNotas:
    def __init__(self, master):
        # Configura la ventana principal y su título
        self.master = master
        self.master.title("Gestión de Notas")

        # Título de la aplicación
        self.label_titulo = tk.Label(master, text="Aplicación de Gestión de Notas", font=("Arial", 16))
        self.label_titulo.pack(pady=10)

        # Muestra las coordenadas del cursor
        self.label_coordenadas = tk.Label(master, text="Coordenadas: (0, 0)")
        self.label_coordenadas.pack()

        # Listbox para mostrar las notas
        self.listbox = tk.Listbox(master, width=50, height=10)
        self.listbox.pack(pady=10)

        # Entrada de texto para agregar notas
        self.entry_nota = tk.Entry(master, width=52)
        self.entry_nota.pack(pady=10)

        # Botón para agregar notas
        self.boton_agregar = tk.Button(master, text="Agregar Nota")
        self.boton_agregar.pack(side=tk.LEFT, padx=5)

        # Botón para eliminar la nota seleccionada
        self.boton_eliminar = tk.Button(master, text="Eliminar Nota")
        self.boton_eliminar.pack(side=tk.LEFT, padx=5)

        # Botón para guardar las notas en un archivo
        self.boton_guardar = tk.Button(master, text="Guardar Notas")
        self.boton_guardar.pack(side=tk.LEFT, padx=5)

        # Botón para cargar las notas desde un archivo
        self.boton_cargar = tk.Button(master, text="Cargar Notas")
        self.boton_cargar.pack(side=tk.LEFT, padx=5)

        # Botón para descargar una imagen desde una URL
        self.boton_descargar = tk.Button(master, text="Descargar Imagen")
        self.boton_descargar.pack(side=tk.LEFT, padx=5)

        # Etiqueta para mostrar la imagen descargada
        self.label_imagen = tk.Label(master)
        self.label_imagen.pack(pady=10)

        # Vincula el evento de clic en la ventana a la función actualizar_coordenadas
        self.master.bind("<Button-1>", self.actualizar_coordenadas)

    def actualizar_coordenadas(self, event):
        # Actualiza las coordenadas del cursor en pantalla al hacer clic
        self.label_coordenadas.config(text=f"Coordenadas: ({event.x}, {event.y})")

    def mostrar_imagen(self, img):
        # Muestra la imagen en la etiqueta label_imagen
        self.img = ImageTk.PhotoImage(img)
        self.label_imagen.config(image=self.img)
