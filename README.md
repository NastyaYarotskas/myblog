# My Blog

Приложение-блог написанное с использованием Spring Boot.

### Как запускать

1. Выполнить команду `./gradlew bootRun `
2. Перейти по ссылку `http://localhost:9090/posts`

### Функционал

Сайт состоит из двух страниц:

1. Страница со всеми постами, на которой присутствуют:
    - превью поста (название, картинка, коротко первый абзац не больше трёх строк);
    - количество комментариев к посту;
    - количество лайков к посту;
    - теги поста;
    - кнопка фильтрации постов по тегу;
    - пагинация (по 10 постов на странице);
    - кнопка добавления поста;

2. Страница поста, на которой присутствуют:
    - название поста;
    - картинка;
    - текст поста, разбитый на абзацы;
    - теги поста;
    - кнопка удаления и редактирования поста (функционал редактирования поста аналогичен его добавлению);
    - кнопка добавления комментария;
    - кнопка лайков поста, при нажатии на которую счётчик лайков увеличивается на единицу;
    - список комментариев (вложенность комментариев делать необязательно);
    - каждый комментарий содержит в себе текст комментария и возможность его редактирования/удаления.