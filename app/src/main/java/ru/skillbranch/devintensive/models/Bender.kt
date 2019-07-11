package ru.skillbranch.devintensive.models

class Bender(var status: Status = Status.NORMAL, var question: Question = Question.NAME) {


    var mistakes: Int = 0

    init {
        mistakes = status.ordinal
    }


    fun askQuestion(): String = when (question) {
        Question.NAME -> Question.NAME.question
        Question.PROFESSION -> Question.PROFESSION.question
        Question.MATERIAL -> Question.MATERIAL.question
        Question.BDAY -> Question.BDAY.question
        Question.SERIAL -> Question.SERIAL.question
        Question.IDLE -> Question.IDLE.question

    }

    fun listenAnswer(answer: String): Pair<String, Triple<Int, Int, Int>> {
        return if (question == Question.IDLE) {
            status = Status.NORMAL
            mistakes = 0
            "Отлично - ты справился\nНа этом все, вопросов больше нет" to status.color
        } else if (question.answers.contains(answer)) {
            question = question.nextQuestion()
            if (question == Question.IDLE) {
                status = Status.NORMAL
                mistakes = 0
                "Отлично - ты справился\nНа этом все, вопросов больше нет" to status.color
            } else {
                "Отлично - ты справился\n${question.question}" to status.color
            }
        } else {
            mistakes++
            if (mistakes < Status.values().size) {
                status = status.nextStatus()
                "Это неправильный ответ\n${question.question}" to status.color
            } else {
                status = Status.NORMAL
                question = Question.NAME
                mistakes = 0
                "Это неправильный ответ. Давай все по новой\n${question.question}" to status.color
            }
        }
    }


    enum class Status(val color: Triple<Int, Int, Int>) {
        NORMAL(Triple(255, 255, 255)),
        WARNING(Triple(255, 120, 0)),
        DANGER(Triple(255, 60, 0)),
        CRITICAL(Triple(255, 255, 0));

        fun nextStatus(): Status {
            return if (this.ordinal < values().lastIndex) {
                values()[this.ordinal + 1]
            } else {
                values()[0]
            }
        }
    }

    enum class Question(val question: String, val answers: List<String>) {
        NAME("Как меня зовут?", listOf("бендер", "bender")) {
            override fun nextQuestion() = PROFESSION
        },
        PROFESSION("Назови мою профессию?", listOf("сгибальщик", "bender")) {
            override fun nextQuestion() = MATERIAL
        },
        MATERIAL("Из чего я сделан?", listOf("металл", "дерево", "metal", "iron", "wood")) {
            override fun nextQuestion() = BDAY
        },
        BDAY("Когда меня создали?", listOf("2993")) {
            override fun nextQuestion() = SERIAL
        },
        SERIAL("Мой серийный номер?", listOf("2716057")) {
            override fun nextQuestion() = IDLE
        },
        IDLE("На этом все, вопросов больше нет", listOf()) {
            override fun nextQuestion() = IDLE
        };


        abstract fun nextQuestion(): Question
    }
}