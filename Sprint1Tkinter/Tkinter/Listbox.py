import tkinter as tk


def mostrar_fruta():
    # Obtener el índice de la fruta seleccionada
    seleccion = listbox_frutas.curselection()

    # Comprobar si se ha seleccionado alguna fruta
    if seleccion:
        # Obtener el texto de la fruta seleccionada usando el índice
        fruta = listbox_frutas.get(seleccion)
        etiqueta_fruta.config(text=f"Fruta seleccionada: {fruta}")
    else:
        etiqueta_fruta.config(text="No has seleccionado ninguna fruta")


# Crear la ventana principal
root = tk.Tk()
root.title("Selecciona una fruta")

# Crear una lista de frutas
frutas = ["Manzana", "Banana", "Naranja"]

# Crear el Listbox con opción de seleccionar solo una fruta a la vez
listbox_frutas = tk.Listbox(root, selectmode=tk.SINGLE)

# Añadir las frutas al Listbox
for fruta in frutas:
    listbox_frutas.insert(tk.END, fruta)

# Crear el botón para mostrar la fruta seleccionada
boton_mostrar = tk.Button(root, text="Mostrar fruta seleccionada", command=mostrar_fruta)

# Crear una etiqueta para mostrar la fruta seleccionada
etiqueta_fruta = tk.Label(root, text="No has seleccionado ninguna fruta")

# Posicionar los widgets en la ventana
listbox_frutas.pack(pady=10)
boton_mostrar.pack(pady=5)
etiqueta_fruta.pack(pady=10)

# Iniciar el bucle de la aplicación
root.mainloop()
