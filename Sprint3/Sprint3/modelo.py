from datetime import datetime
import os
import random




class GameModel:
    def __init__(self, difficulty):
        self.difficulty = difficulty
        self.board = []
        self.rows = 0
        self.cols = 0
        self.time_started = False  # Para controlar el inicio del temporizador
        self.time_elapsed = 0  # Tiempo transcurrido en segundos
        self.scores = self.load_scores()  # Cargar las puntuaciones al iniciar
        self._generate_board()  # Generar el tablero en la inicialización

    def _generate_board(self):
        """Generar el tablero con posiciones aleatorias basadas en la dificultad"""
        if self.difficulty == "facil":
            num_pairs = 8  # 8 pares de cartas = 16 cartas
        elif self.difficulty == "medio":
            num_pairs = 18  # 18 pares de cartas = 36 cartas
        elif self.difficulty == "dificil":
            num_pairs = 32  # 32 pares de cartas = 64 cartas
        else:
            raise ValueError("Dificultad desconocida")

        # Generar una lista de cartas, que será una lista de números representando las cartas
        cards = [i for i in range(num_pairs)] * 2  # Crear los pares de cartas (duplicamos las cartas)
        random.shuffle(cards)  # Barajar las cartas
        self.board = cards  # Asignar el tablero

        # El tamaño del tablero depende de la cantidad de cartas generadas
        total_cards = len(self.board)

        # Ajustamos las filas y columnas dependiendo de la cantidad total de cartas
        if total_cards == 16:
            self.rows = 4  # 4x4 para fácil
            self.cols = 4
        elif total_cards == 36:
            self.rows = 6  # 6x6 para medio
            self.cols = 6
        elif total_cards == 64:
            self.rows = 8  # 8x8 para difícil
            self.cols = 8

    def start_timer(self):
        """Iniciar el temporizador si no ha comenzado"""
        if not self.time_started:
            self.time_started = True
            self.time_elapsed = 0  # Resetear el tiempo
            self.update_timer()  # Llamar a update_timer para empezar a contar

    def update_timer(self):
        """Actualizar el temporizador cada segundo"""
        if self.time_started:
            self.time_elapsed += 1
        return self.time_elapsed  # Retornar el valor actualizado de tiempo

    def save_score(self, player_name, moves, time_taken):
        """Guardar la puntuación en el archivo ranking.txt y en memoria"""
        fecha = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        score_line = f"{player_name},{self.difficulty},{moves},{time_taken},{fecha}\n"

        # Guardar la puntuación en el archivo
        with open("ranking.txt", "a") as file:
            file.write(score_line)

        # Guardar la puntuación en memoria
        self.scores[self.difficulty].append({
            "name": player_name,
            "difficulty": self.difficulty,
            "moves": moves,
            "time_taken": time_taken,
            "date": fecha
        })

        # Ordenar y mantener solo las 3 mejores puntuaciones
        self.sort_scores()

    def sort_scores(self):
        """Ordenar las puntuaciones por dificultad y mantener las 3 mejores"""
        # Ordenamos por dificultad, luego por el número de movimientos (menos es mejor), y finalmente por el tiempo (menos es mejor)
        for difficulty in self.scores:
            # Ordenamos por número de movimientos (menos es mejor) y luego por tiempo (menos es mejor)
            self.scores[difficulty] = sorted(self.scores[difficulty], key=lambda x: (x['moves'], x['time_taken']))[:3]

        # Depuración: Imprimir las puntuaciones ordenadas
        print("Puntuaciones ordenadas:", self.scores)

    def load_scores(self):
        """Cargar las puntuaciones desde el archivo ranking.txt"""
        # Si el archivo no existe, retornamos las puntuaciones vacías
        scores = {"facil": [], "medio": [], "dificil": []}

        if not os.path.exists("ranking.txt"):
            return scores  # Si el archivo no existe, retornamos las puntuaciones vacías

        # Leer el archivo y cargar las puntuaciones
        with open("ranking.txt", "r") as file:
            for line in file:
                name, difficulty, moves, time_taken, date = line.strip().split(",")
                # Añadir la puntuación al diccionario correspondiente
                scores[difficulty].append({
                    "name": name,
                    "difficulty": difficulty,
                    "moves": int(moves),
                    "time_taken": int(time_taken),
                    "date": date
                })


        for difficulty in scores:
            scores[difficulty] = sorted(scores[difficulty], key=lambda x: (x['moves'], x['time_taken']))[:3]

        return scores