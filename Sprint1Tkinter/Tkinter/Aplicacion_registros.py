import tkinter as tk
from tkinter import messagebox, filedialog
import json


class AplicacionRegistros:
    def __init__(self, master):
        self.master = master
        self.master.title("Registro de Usuarios")

        # Variables
        self.users = []

        # Nombre
        self.name_label = tk.Label(master, text="Nombre:")
        self.name_label.pack()
        self.name_entry = tk.Entry(master)
        self.name_entry.pack()

        # Edad (con Scale)
        self.age_label = tk.Label(master, text="Edad:")
        self.age_label.pack()
        self.age_scale = tk.Scale(master, from_=0, to=100, orient=tk.HORIZONTAL)
        self.age_scale.pack()

        # Género
        self.gender_label = tk.Label(master, text="Género:")
        self.gender_label.pack()
        self.gender_var = tk.StringVar(value="Masculino")  # Valor por defecto
        self.gender_male = tk.Radiobutton(master, text="Masculino", variable=self.gender_var, value="Masculino")
        self.gender_female = tk.Radiobutton(master, text="Femenino", variable=self.gender_var, value="Femenino")
        self.gender_other = tk.Radiobutton(master, text="Otro", variable=self.gender_var, value="Otro")
        self.gender_male.pack()
        self.gender_female.pack()
        self.gender_other.pack()

        # Botón para añadir usuario
        self.add_button = tk.Button(master, text="Añadir Usuario", command=self.add_user)
        self.add_button.pack()

        # Lista de usuarios
        self.user_listbox = tk.Listbox(master, width=50)
        self.user_listbox.pack()

        # Barra de desplazamiento
        self.scrollbar = tk.Scrollbar(master)
        self.scrollbar.pack(side=tk.RIGHT, fill=tk.Y)
        self.user_listbox.config(yscrollcommand=self.scrollbar.set)
        self.scrollbar.config(command=self.user_listbox.yview)

        # Botón para eliminar usuario
        self.delete_button = tk.Button(master, text="Eliminar Usuario", command=self.delete_user)
        self.delete_button.pack()

        # Botón para salir
        self.exit_button = tk.Button(master, text="Salir", command=master.quit)
        self.exit_button.pack()

        # Menú
        self.menu = tk.Menu(master)
        master.config(menu=self.menu)

        self.file_menu = tk.Menu(self.menu)
        self.menu.add_cascade(label="Archivo", menu=self.file_menu)
        self.file_menu.add_command(label="Guardar Lista", command=self.save_list)
        self.file_menu.add_command(label="Cargar Lista", command=self.load_list)

    def add_user(self):
        name = self.name_entry.get()
        age = self.age_scale.get()
        gender = self.gender_var.get()

        if name:
            user_info = f"{name}, Edad: {age}, Género: {gender}"
            self.user_listbox.insert(tk.END, user_info)
            self.users.append({"nombre": name, "edad": age, "género": gender})
            self.name_entry.delete(0, tk.END)  # Limpiar entrada
        else:
            messagebox.showwarning("Advertencia", "Por favor, introduce un nombre.")

    def delete_user(self):
        try:
            selected_index = self.user_listbox.curselection()[0]
            self.user_listbox.delete(selected_index)
            del self.users[selected_index]
        except IndexError:
            messagebox.showwarning("Advertencia", "Por favor, selecciona un usuario para eliminar.")

    def save_list(self):
        file_path = filedialog.asksaveasfilename(defaultextension=".json", filetypes=[("JSON files", "*.json")])
        if file_path:
            with open(file_path, 'w') as file:
                json.dump(self.users, file)
            messagebox.showinfo("Éxito", "La lista ha sido guardada.")

    def load_list(self):
        file_path = filedialog.askopenfilename(filetypes=[("JSON files", "*.json")])
        if file_path:
            with open(file_path, 'r') as file:
                self.users = json.load(file)
            self.user_listbox.delete(0, tk.END)  # Limpiar lista
            for user in self.users:
                user_info = f"{user['nombre']}, Edad: {user['edad']}, Género: {user['género']}"
                self.user_listbox.insert(tk.END, user_info)
            messagebox.showinfo("Éxito", "La lista ha sido cargada.")


if __name__ == "__main__":
    root = tk.Tk()
    app = AplicacionRegistros(root)
    root.mainloop()
