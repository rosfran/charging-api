INSERT INTO public.network (id, name, user_id)
VALUES (1, 'Jake Network', 2);

INSERT INTO public.solar_grid (id, age, name, power_output, network_id)
VALUES (1, 949,'Sao Paulo', 293.2, 1 );

INSERT INTO public.solar_grid (id, age, name, power_output, network_id)
VALUES (2, 823,'Brasilia', 1293.2, 1 );

SELECT setval('sequence_network', max(id)) FROM public.network;

SELECT setval('sequence_solar_grid', max(id)) FROM public.solar_grid;
