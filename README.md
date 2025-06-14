# Java Voice Assistant

A personal voice assistant built with Java.

## Features
- Voice recognition and synthesis
- Command handling system
- Weather information
- Time and date
- Persistent preferences
- Reminders
- NLP for better understanding

## Requirements
- Java 11+
- Gradle
- OpenWeatherMap API key (for weather)

## Setup
1. Clone the repository
2. Get an API key from [OpenWeatherMap](https://openweathermap.org/) and add it to `WeatherService.java`
3. Run `./gradlew build`
4. Run `./build.sh`

## Usage
- Say the wake word (configured in `config.properties`) to activate
- Try commands like:
    - "What time is it?"
    - "What's today's date?"
    - "What's the weather in London?"
    - "Remember that my favorite color is blue"
    - "What is my favorite color?"
    - "Remind me to call mom at 12-25 15:00"
    - "Do I have any reminders?"

## Configuration
Edit `src/main/resources/config.properties` to:
- Change the assistant name
- Change the wake word
- Change the default voice