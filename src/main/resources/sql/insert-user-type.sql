insert into agu_user_types (id, create_date_time, name, update_date_time, deleted, creator_id)
values (gen_random_uuid(), now(), 'Студент', now(), false, gen_random_uuid()),
       (gen_random_uuid(), now(), 'Преподаватель', now(), false, gen_random_uuid()),
       (gen_random_uuid(), now(), 'Куратор', now(), false, gen_random_uuid());
