INSERT INTO public.role (id, "solarGrid") VALUES (1, 'ROLE_USER');
INSERT INTO public.role (id, "solarGrid") VALUES (2, 'ROLE_ADMIN');

SELECT setval('sequence_role', max(id)) FROM public.role;