import tkinter as tk


#Funciones para los botones
def mostrar_contenido():
    etiqueta_resultado.config(text="Contenido: " + entrada.get())


def borrar_contenido():
    entrada.delete(0, tk.END)
    etiqueta_resultado.config(text="Contenido borrado")

# Crear la ventana principal
root = tk.Tk()
root.title("Interfaz con dos Frames")
root.geometry("300x200")

# Frame superior
frame_superior = tk.Frame(root)
frame_superior.pack(pady=10)

# Etiquetas y campo de entrada en el frame superior
etiqueta1 = tk.Label(frame_superior, text="Etiqueta 1:")
etiqueta1.grid(row=0, column=0, padx=5, pady=5)

etiqueta2 = tk.Label(frame_superior, text="Etiqueta 2:")
etiqueta2.grid(row=1, column=0, padx=5, pady=5)

entrada = tk.Entry(frame_superior)
entrada.grid(row=0, column=1, padx=5, pady=5, rowspan=2)

# Frame inferior
frame_inferior = tk.Frame(root)
frame_inferior.pack(pady=10)

# Botones en el frame inferior
boton_mostrar = tk.Button(frame_inferior, text="Mostrar contenido", command=mostrar_contenido)
boton_mostrar.grid(row=0, column=0, padx=5, pady=5)

boton_borrar = tk.Button(frame_inferior, text="Borrar contenido", command=borrar_contenido)
boton_borrar.grid(row=0, column=1, padx=5, pady=5)

# Etiqueta para mostrar el resultado
etiqueta_resultado = tk.Label(root, text="")
etiqueta_resultado.pack(pady=10, padx=10)

# Iniciar el loop principal de la aplicación
root.mainloop()