import tkinter as tk


def cambiar_color():
    # Obtener el color seleccionado
    color_seleccionado = color_var.get()

    # Cambiar el color de fondo de la ventana al color seleccionado
    if color_seleccionado == "Rojo":
        root.config(bg="red")
    elif color_seleccionado == "Verde":
        root.config(bg="green")
    elif color_seleccionado == "Azul":
        root.config(bg="blue")

#Crear la ventana principal
root = tk.Tk()
root.title("Elige tu color favorito")
root.geometry("300x200")

# Variable para almacenar la opción seleccionada
color_var = tk.StringVar()
color_var.set("Rojo")  # Valor inicial

# Crear los botones de opción (Radiobutton)
radio_rojo = tk.Radiobutton(root, text="Rojo", variable=color_var, value="Rojo", command=cambiar_color)
radio_verde = tk.Radiobutton(root, text="Verde", variable=color_var, value="Verde", command=cambiar_color)
radio_azul = tk.Radiobutton(root, text="Azul", variable=color_var, value="Azul", command=cambiar_color)

# Posicionar los botones de opción en la ventana
radio_rojo.pack(anchor='w', pady=5)
radio_verde.pack(anchor='w', pady=5)
radio_azul.pack(anchor='w', pady=5)

# Iniciar el bucle de la aplicación
root.mainloop()
