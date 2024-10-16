import tkinter as tk


def actualizar_aficiones():
    # Recopilar las aficiones seleccionadas
    aficiones_seleccionadas = []
    if leer_var.get():
        aficiones_seleccionadas.append("Leer")
    if deporte_var.get():
        aficiones_seleccionadas.append("Deporte")
    if musica_var.get():
        aficiones_seleccionadas.append("Música")

    # Actualizar el texto de la etiqueta con las aficiones seleccionadas
    if aficiones_seleccionadas:
        seleccionadas.set(f"Aficiones seleccionadas: {', '.join(aficiones_seleccionadas)}")
    else:
        seleccionadas.set("No has seleccionado ninguna afición")


# Crear la ventana principal e inicializar sus propiedades
root = tk.Tk()
root.title("Aficiones")

# Variables para almacenar el estado de las casillas de verificación
leer_var = tk.BooleanVar()
deporte_var = tk.BooleanVar()
musica_var = tk.BooleanVar()

# Crear las casillas de verificación
check_leer = tk.Checkbutton(root, text="Leer", variable=leer_var, command=actualizar_aficiones)
check_deporte = tk.Checkbutton(root, text="Deporte", variable=deporte_var, command=actualizar_aficiones)
check_musica = tk.Checkbutton(root, text="Música", variable=musica_var, command=actualizar_aficiones)

# Crear una etiqueta para mostrar las aficiones seleccionadas
seleccionadas = tk.StringVar()
seleccionadas.set("No has seleccionado ninguna afición")
label_seleccionadas = tk.Label(root, textvariable=seleccionadas)

# Posicionar los widgets en la ventana
check_leer.pack(anchor='w')
check_deporte.pack(anchor='w')
check_musica.pack(anchor='w')
label_seleccionadas.pack(pady=10)

# Iniciar el bucle de la aplicación
root.mainloop()