import tkinter as tk

# Crear la ventana principal
root = tk.Tk()
root.title("Text con Scrollbar")
root.geometry("500x500")

# Crear un marco para contener el Text y la Scrollbar
frame = tk.Frame(root)
frame.pack(pady=10)

# Crear el widget Text
text_area = tk.Text(frame, width=50, height=25)
text_area.pack(side=tk.LEFT)

# Crear la Scrollbar y asociarla al Text
scrollbar = tk.Scrollbar(frame, command=text_area.yview)
scrollbar.pack(side=tk.RIGHT, fill=tk.Y)

# Configurar el Text para que use la Scrollbar
text_area.config(yscrollcommand=scrollbar.set)

# Añadir un texto largo al Text widget
long_text = (
    "Python es un lenguaje de programación de alto nivel, interpretado y de propósito general. "
    "Es conocido por su sintaxis clara y legibilidad, lo que lo hace ideal tanto para principiantes como para desarrolladores experimentados. "
    "Python soporta múltiples paradigmas de programación, incluyendo programación orientada a objetos, programación imperativa y programación funcional.\n\n"

    "Una de las grandes ventajas de Python es su extensa biblioteca estándar y su comunidad activa. "
    "Existen numerosas bibliotecas y frameworks, como Django para desarrollo web y Pandas para análisis de datos, "
    "que facilitan la implementación de tareas complejas. Python también es muy popular en campos como la ciencia de datos, "
    "inteligencia artificial y automatización, debido a su capacidad para manejar grandes volúmenes de datos de manera eficiente.\n\n"

    "Además, Python es un lenguaje multiplataforma, lo que significa que se puede ejecutar en diferentes sistemas operativos como Windows, "
    "macOS y Linux. Esto, junto con su fácil aprendizaje y aplicación, ha contribuido a su crecimiento y popularidad en la comunidad de programación. "
    "La filosofía de Python se centra en la simplicidad y la elegancia del código, fomentando un desarrollo rápido y eficiente.\n"
)

# Insertar el texto largo en el widget Text
text_area.insert(tk.END, long_text)

# Iniciar el bucle principal de la aplicación
root.mainloop()
