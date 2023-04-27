INSERT INTO public.user (id, first_name, last_name, username, "password")
VALUES (1, 'John', 'Doe', 'johndoe', '$2a$10$MMOkMuO8zVcXl8YH2GrZSOYf/9zeC/sznGHRVzAq0T8.tzet7QJWq');

SELECT setval('sequence_user', max(id)) FROM public.user;