# Flipping Card Game 🃏

**Flipping Card Game** is a memory and focus-based card matching game built with **JavaFX**. Players attempt to match paired cards across various difficulty levels and visual themes.

---

## 🚀 Features

- **Dynamic Difficulty Levels:**
    - **Easy:** 8 Cards (2x4 Grid) — 2.0s card preview delay.
    - **Medium:** 24 Cards (4x6 Grid) — 1.5s card preview delay[cite: 3].
    - **Hard:** 48 Cards (6x8 Grid) — 0.5s card preview delay[cite: 3].

- **Multiple Visual Themes:**
    - **Built-in Themes:** Pokemon and Animal themes[cite: 3].
    - **Custom Theme:** Load your own custom images directly from your computer gallery.

- **In-Game Metrics & Experience:**
    - **Timer:** Tracks your completion time in seconds[cite: 3].
    - **Wrong Attempts Counter:** Counts unmatched flip attempts[cite: 3].
    - **Sound Effects:** Dedicated audio cues for clicks, successful matches, wrong picks, and already opened cards[cite: 3].
    - **Responsive Grid:** Dynamic layout that automatically resizes and aligns based on grid size and window dimensions[cite: 3].

---

## 🛠️ Tech Stack

- **Language:** Java 17+[cite: 3]
- **GUI Framework:** JavaFX[cite: 3]
- **UI Design:** FXML & Scene Builder[cite: 3]
- **Build Tool:** Maven

---

## 📦 Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven (or wrapper `./mvnw`)[cite: 1]

### Installation

1. **Clone the repository:**
```bash
       git clone git@github.com:Beykn/flipping_card_game.git
```

### Packaging for macOS (.dmg)

To build a standalone .dmg installer application for macOS:

1. Build the executable JAR package:

```bash
    ./mvnw clean package
```
1. Generate the .dmg package using jpackage:

```bash
    jpackage --name FlippingCardGame \
  --input target/ \
  --main-jar flipping_card_game-1.0-SNAPSHOT.jar \
  --main-class com.example.flipping_card_game.Main \
  --type dmg \
  --mac-package-identifier com.example.flippingcardgame
```