package org.diploma.user.exception;

public interface ExceptionMessageConstants {
  String NAME_PATTERN_NOT_VALID = "Имя может состоять только из русских и англиских букв, и иметь размер от 2 до 100 симвоолов";
  String SURNAME_PATTERN_NOT_VALID = "Фимилия может состоять только из русских и англиских букв, и иметь размер от 2 до 100 симвоолов";
  String LOGIN_PATTERN_NOT_VALID = "Логин должен быть длиной от 3 до 200 символов, начинаться с буквы, и может содержать только английские буквы, цифры, точки, дефисы или подчеркивания";
  String PASSWORD_PATTERN_NOT_VALID = "Пароль должен быть длиной от 8 символов, содержать только английские буквы, хотя бы одну заглавную и строчную букву, цифру и один из следующих спецсимволов: #, ?, !, @, $, %, ^, &, *, -, +.";
  String PHONE_PATTERN_NOT_VALID = "Номер телефона должен начинаться с +7 и иметь длину 12 символов";
  String USER_TYPE_NULL = "Тип пользователя не должен быть пустым";
  String USER_TYPE_EXIST = "Тип пользователя с таким названием уже существует.";
  String USER_TYPE_NOT_VALID = "Название группы может состоять только из русских и англиских букв, и иметь размер от 2 до 100 симвоолов";
  String USER_TYPE_NOT_FOUND = "Такого типа пользователя не существует";
  String GROUP_NULL = "Группа не должена быть пустой";
  String GROUP_NOT_FOUND = "Такой группы не существует";
  String FILE_NOT_FOUND = "Файл не был передан.";
  String FILE_ERROR = "Ошибка при загрузке файла.";
  String FILE_TYPE_ERROR = "Только png, jpg, jpeg.";
  String USER_CREATOR_NOT_FOUND = "Ошибка на сервере, обратитесь к администатору";
  String USER_NOT_FOUND = "Пользователя не существует.";
  String TYPE_NOT_FOUND = "Такого типа пользователя не существует.";
  String LOGIN_ALREADY_EXIST = "Логин уже используется";
  String LOGIN_ERROR = "Невеный логин или пароль";
  String TOKEN_NOT_VALID = "Не валидный токен";
  String USER_DELETION_REASON_NOT_VALID = "Причина деактивации должна быть от 5 до 100 символов.";

  String ERROR_UPDATE_PASSWORD = "Ошибка при смене пароля.";
  String ERROR_GET_ROLES = "Ошибка получения ролей для пользователя";
  String ERROR_UPDATE_ROLES = "Ошибка обновления ролей. Повторите попытку.";

  String GROUP_NAME_NOT_VALID = "Название группы может состоять только из русских и англиских букв, и иметь размер от 2 до 100 симвоолов";
  String GROUP_SHORT_NAME_NOT_VALID = "Короткое название группы может состоять только из русских, англиских букв и цифр, и иметь размер от 2 до 15 симвоолов";
  String GROUP_COUNT_COURSE_NOT_VALID = "Количество курсов должно быть от 1 до 8 включительно.";

  String NEWS_NOT_FOUND = "Ошибка! Такой новости не существует.";

  String SKILL_NAME_NOT_VALID = "Название скила должно быть от 5 до 100 символов ";

  String TASK_NOT_FOUND = "Такой задачи не существует.";
  String TEST_CASE_NOT_FOUND = "Такого кейса не существует.";
  String TASK_NAME_NOT_VALID = "Название задачи не должно быть пустым.";
  String TASK_DESCRIPTION_NOT_VALID = "Описание задачи не должно быть пустым.";
  String TASK_CODE_NOT_VALID = "Код задачи не должен быть пустым.";
  String TASK_ESTIMATION_NOT_VALID = "Оценка задачи не должна быть пустым.";
  String TASK_ESTIMATION_NOT_VALID_POSITIVE = "Оценка задачи должна быть больше 0.";
  String TASK_TIME_LIMIT_NOT_VALID = "Ограничение по времени должно быть больше 0.";
  String TASK_MEMORY_LIMIT_NOT_VALID = "Ограничение по памяти должно быть больше 0.";
  String TASK_LANGUAGE_NOT_VALID = "Выберите язык на котором написан код для задачи";
  String TASK_NOT_IN_COURSE = "Задачи нет в курсе";
  String TASK_NOT_ACCESS = "Нет доступа к курсу";
  String TASK_ID_NOT_VALID= "ID задачи не должно быть пустым.";
  String COURSE_ID_NOT_VALID= "ID курса не должно быть пустым.";

  String ATTEMPT_EXIST = "Ваше решение уже находится на проверке или задача уже решена.";
  String ATTEMPT_NOT_FOUND = "Нет такой истории.";
  String ATTEMPT_STATUS_NOT_REVIEW = "Попытка не находится в статусе ревью";

  String ATTEMPT_APPROVE_ESTIMATION_NOT_VALID = "Количество баллов должно быть от 0 до %s";

  String REASON_NOT_VALID = "Причина должна быть заполнена";

  String REASON_NOT_VALID2 = "Должна быть заполнена причина снижения оценки";

  String QUEUE_TASK_WITH_TASK_ID_EXIST = "Тест кейсы уже запущены.";

  String COURSE_NOT_FOUND = "Такого курса не существует";
  String COURSE_LANGUAGE_NOT_VALID = "Укажите хотя бы 1 язык программирования";
  String COURSE_NAME_NOT_VALID = "Название курса не должно быть пустым";
  String COURSE_DESCRIPTION_NOT_VALID = "Описание курса не должно быть пустым";
  String TASK_EXIST_IN_COURSE = "Задача уже добавлена в курс";
}
