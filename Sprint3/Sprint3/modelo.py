import threading
import time
from recursos import descargar_imagen
import random


class GameModel:
    def __init__(self, difficulty):
        self.difficulty = difficulty
        self.board = self._generate_board(difficulty)
        self.hidden_image = None
        self.images = {}
        self.images_loaded = False
        self.selected_positions = []
        self.moves = 0
        self.start_time = None
        self.is_game_over = False
        self._load_images()

    def _load_images(self):
        url_base = ""

        def load_images_thread():
            self.hidden_image = descargar_imagen(url_base + "hidden.png")
            unique_image_ids = []
            for row in self.board:
                for image_id in row:
                    if image_id not in unique_image_ids:
                        unique_image_ids.append(image_id)
            for image_id in unique_image_ids:
                self.images[image_id] = descargar_imagen(f"{url_base}{image_id}.png")
            self.images_loaded = True

        threading.Thread(target=load_images_thread, daemon=True).start()

    def _generate_board(self, difficulty):
        if difficulty == "facil":
            num_pairs = 4
        elif difficulty == "medio":
            num_pairs = 8
        elif difficulty == "dificil":
            num_pairs = 12
        else:
            raise ValueError("Dificultad no válida")

        image_ids = list(range(1, num_pairs + 1)) * 2
        random.shuffle(image_ids)
        board_size = int(len(image_ids) ** 0.5)
        board = [image_ids[i * board_size:(i + 1) * board_size] for i in range(board_size)]
        return board

    def start_timer(self):
        if not self.start_time:
            self.start_time = time.time()

    def check_match(self, pos1, pos2):
        row1, col1 = pos1
        row2, col2 = pos2
        return self.board[row1][col1] == self.board[row2][col2]

    def is_game_complete(self):
        for row in self.board:
            for image_id in row:
                if image_id != 0:
                    return False
        self.is_game_over = True
        return True

    def mark_pair_found(self, pos1, pos2):
        row1, col1 = pos1
        row2, col2 = pos2
        self.board[row1][col1] = 0
        self.board[row2][col2] = 0

    def increment_move_count(self):
        self.moves += 1

    def get_elapsed_time(self):
        if self.start_time:
            return int(time.time() - self.start_time)
        return 0

    def save_score(self, player_name):
        with open("ranking.txt", "a") as file:
            score_data = f"{player_name},{self.difficulty},{self.moves},{self.get_elapsed_time()}\n"
            file.write(score_data)

    def load_scores(self):
        scores = {"facil": [], "medio": [], "dificil": []}
        try:
            with open("ranking.txt", "r") as file:
                for line in file:
                    name, difficulty, moves, time_taken = line.strip().split(",")
                    scores[difficulty].append({
                        "name": name,
                        "moves": int(moves),
                        "time": int(time_taken)
                    })
            for difficulty in scores:
                scores[difficulty] = sorted(scores[difficulty], key=lambda x: (x["moves"], x["time"]))[:3]
        except FileNotFoundError:
            pass
        return scores
