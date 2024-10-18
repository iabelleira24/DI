import tkinter as tk
from tkinter import messagebox

def abrir_archivo():
    # Aquí puedes añadir la lógica para abrir un archivo
    pass

def salir():
    root.quit()

def acerca_de():
    messagebox.showinfo("Acerca de", "Este es un programa de ejemplo usando Tkinter.")

# Crear la ventana principal
root = tk.Tk()
root.title("Ejemplo de Menú")
root.geometry("300x200")

# Crear la barra de menú
barra_menu = tk.Menu(root)

# Crear el menú "Archivo"
menu_archivo = tk.Menu(barra_menu, tearoff=0)
menu_archivo.add_command(label="Abrir", command=abrir_archivo)
menu_archivo.add_separator()
menu_archivo.add_command(label="Salir", command=salir)
barra_menu.add_cascade(label="Archivo", menu=menu_archivo)

# Crear el menú "Ayuda"
menu_ayuda = tk.Menu(barra_menu, tearoff=0)
menu_ayuda.add_command(label="Acerca de", command=acerca_de)
barra_menu.add_cascade(label="Ayuda", menu=menu_ayuda)

# Configurar la barra de menú en la ventana
root.config(menu=barra_menu)

# Iniciar el bucle principal de la aplicación
root.mainloop()
