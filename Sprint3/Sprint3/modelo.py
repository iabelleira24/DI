from datetime import datetime
import os
import random

class GameModel:
    def __init__(self, difficulty_level):
        self.difficulty_level = difficulty_level
        self.board = []
        self.rows = 0
        self.cols = 0
        self.timer_active = False
        self.elapsed_time = 0
        self.high_scores = self.load_high_scores()
        self._initialize_board()

    def _initialize_board(self):
        """Crea el tablero según la dificultad."""
        pairs = {"facil": 8, "medio": 18, "dificil": 32}.get(self.difficulty_level)
        if not pairs:
            raise ValueError("Nivel de dificultad no reconocido")

        cards = [i for i in range(pairs)] * 2
        random.shuffle(cards)
        self.board = cards
        self.rows, self.cols = {16: (4, 4), 36: (6, 6), 64: (8, 8)}.get(len(cards), (0, 0))

    def start_timer(self):
        """Inicia el temporizador."""
        if not self.timer_active:
            self.timer_active = True
            self.elapsed_time = 0

    def _tick_timer(self):
        """Incrementa el tiempo si el temporizador está activo."""
        if self.timer_active:
            self.elapsed_time += 1
        return self.elapsed_time

    def save_high_score(self, player_name, moves, time_spent):
        """Guarda la puntuación en archivo y memoria."""
        timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        record = f"{player_name},{self.difficulty_level},{moves},{time_spent},{timestamp}\n"

        with open("ranking.txt", "a") as file:
            file.write(record)

        self.high_scores[self.difficulty_level].append({
            "name": player_name,
            "difficulty": self.difficulty_level,
            "moves": moves,
            "time_spent": time_spent,
            "date": timestamp
        })
        self._organize_high_scores()

    def _organize_high_scores(self):
        """Mantiene ordenadas las 3 mejores puntuaciones por dificultad."""
        for level in self.high_scores:
            self.high_scores[level] = sorted(
                self.high_scores[level],
                key=lambda x: (x['moves'], x['time_spent'])
            )[:3]

    def load_high_scores(self):
        """Carga puntuaciones desde el archivo."""
        scores = {"facil": [], "medio": [], "dificil": []}

        if not os.path.isfile("ranking.txt"):
            return scores

        with open("ranking.txt", "r") as file:
            for line in file:
                name, difficulty, moves, time_spent, date = line.strip().split(",")
                scores[difficulty].append({
                    "name": name,
                    "difficulty": difficulty,
                    "moves": int(moves),
                    "time_spent": int(time_spent),
                    "date": date
                })

        for level in scores:
            scores[level] = sorted(scores[level], key=lambda x: (x['moves'], x['time_spent']))[:3]

        return scores
