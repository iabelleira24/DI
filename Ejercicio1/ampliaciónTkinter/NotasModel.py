class NotasModel:
    def __init__(self):
        # Inicializa la lista de notas y carga las notas guardadas
        self.notas = []
        self.cargar_notas()

    def agregar_nota(self, nueva_nota):
        # Agrega una nueva nota a la lista
        self.notas.append(nueva_nota)

    def eliminar_nota(self, indice):
        # Elimina la nota en el índice especificado, si es válido
        if 0 <= indice < len(self.notas):
            del self.notas[indice]

    def obtener_notas(self):
        # Devuelve todas las notas actuales
        return self.notas

    def guardar_notas(self):
        # Guarda todas las notas en un archivo de texto
        with open('notas.txt', 'w') as file:
            for nota in self.notas:
                file.write(nota + '\n')

    def cargar_notas(self):
        # Carga las notas desde un archivo, si existe
        try:
            with open('notas.txt', 'r') as file:
                self.notas = [line.strip() for line in file.readlines()]
        except FileNotFoundError:
            self.notas = []
