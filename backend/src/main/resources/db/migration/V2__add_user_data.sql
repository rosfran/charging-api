INSERT INTO public.user (id, first_name, last_name, username, "password")
VALUES (1, 'John', 'Doe', 'johndoe', '$2a$10$MMOkMuO8zVcXl8YH2GrZSOYf/9zeC/sznGHRVzAq0T8.tzet7QJWq');
INSERT INTO public.user (id, first_name, last_name, username, "password")
VALUES (2, 'Jake', 'Vando', 'jake', '$2y$04$IxoCwBtRGeBixa0ERJvn9ueeomjLy4aaGPzwtbROWx7f4LA2iSCeG');

SELECT setval('sequence_user', max(id)) FROM public.user;