insert into especialidad (esp_nombre) values ('Familia');
insert into especialidad (esp_nombre) values ('Civil');
insert into especialidad (esp_nombre) values ('Penal');
insert into especialidad (esp_nombre) values ('Laboral');

insert into juzgado (juz_nombre, esp_id_juz) values ('Juzgado pimero de familia', 1);
insert into juzgado (juz_nombre, esp_id_juz) values ('Juzgado segundo de familia', 1);
insert into juzgado (juz_nombre, esp_id_juz) values ('Juzgado primero civil', 2);
insert into juzgado (juz_nombre, esp_id_juz) values ('Juzgado segundo civil', 2);



insert into tipo_proceso (tip_pro_nombre, esp_id_tip_pro) values ('INV. PATERNIDAD', 1);
insert into tipo_proceso (tip_pro_nombre, esp_id_tip_pro) values ('ALIMENTOS', 1);
insert into tipo_proceso (tip_pro_nombre, esp_id_tip_pro) values ('IMP. PATERNIDADDD', 2);
insert into tipo_proceso (tip_pro_nombre, esp_id_tip_pro) values ('CUSTODIAAA', 2);




insert into proceso ( pro_create_at, pro_radicado, pro_demandante, pro_demandado, pro_fecha_reparto, pro_ultima_actuacion, pro_estado_actual, pro_estado, tip_pro_id_pro, juz_id_pro, pro_prioritario) values ('04-03-2019', '20190020001123456789012', ' Esteban Salcedo Alvarez', 'Alejandra Lozano', '02-03-2019', '', '', true, 1, 2, false);
insert into proceso ( pro_create_at, pro_radicado, pro_demandante, pro_demandado, pro_fecha_reparto, pro_ultima_actuacion, pro_estado_actual, pro_estado, tip_pro_id_pro, juz_id_pro, pro_prioritario) values ('15-12-2019',  '20180030002123456789012', ' Angel Roberto', 'Doris Stella Alvarez', '04-03-2019', '', '', true, 2, 2, false);
insert into proceso ( pro_create_at, pro_radicado, pro_demandante, pro_demandado, pro_fecha_reparto, pro_ultima_actuacion, pro_estado_actual, pro_estado, tip_pro_id_pro, juz_id_pro, pro_prioritario) values ('9-07-2019',  '20180030003123456789012', ' Angel Roberto', 'Julian Castillo', '04-03-2019', '', '', true, 2, 2, false);





insert into termino (ter_nombre, ter_numero_dias, ter_basico, tip_pro_id_ter, esp_id_ter) values ('admision', 30, true, 1, 1);
insert into termino (ter_nombre, ter_numero_dias, ter_basico, tip_pro_id_ter, esp_id_ter) values ('subsanar demanda', 15, true, 1, 1);
insert into termino (ter_nombre, ter_numero_dias, ter_basico, tip_pro_id_ter, esp_id_ter) values ('admision', 15, true, 2, 1);
insert into termino (ter_nombre, ter_numero_dias, ter_basico, tip_pro_id_ter, esp_id_ter) values ('subsanar demanda', 15, true, 2, 1);
insert into termino (ter_nombre, ter_numero_dias, ter_basico, tip_pro_id_ter, esp_id_ter) values ('adminion', 15, true, 3, 3);
insert into termino (ter_nombre, ter_numero_dias, ter_basico, tip_pro_id_ter, esp_id_ter) values ('notificacion demandado', 15, true, 2, 1);
insert into termino (ter_nombre, ter_numero_dias, ter_basico, tip_pro_id_ter, esp_id_ter) values ('notificacion demandado', 15, true, 1, 1);
insert into termino (ter_nombre, ter_numero_dias, ter_basico, tip_pro_id_ter, esp_id_ter) values ('Termino 121', 365, false, 2, 1);
insert into termino (ter_nombre, ter_numero_dias, ter_basico, tip_pro_id_ter, esp_id_ter) values ('Termino 121', 365, false, 1, 1);




insert into detalle_termino (det_ter_dias_habiles, det_ter_fecha_incial, det_ter_fecha_final, ter_id_det_ter, pro_id_det_ter) values (true, '02-03-2019', '02-03-2020', 1, 1);
insert into detalle_termino (det_ter_dias_habiles, det_ter_fecha_incial, det_ter_fecha_final, ter_id_det_ter, pro_id_det_ter) values (true, '02-03-2019', '02-03-2020', 2, 1);
insert into detalle_termino (det_ter_dias_habiles, det_ter_fecha_incial, det_ter_fecha_final, ter_id_det_ter, pro_id_det_ter) values (true, '02-03-2019', '02-03-2020', 3, 1);



insert into usuario (usu_username, usu_create_at, usu_password, usu_enabled, usu_nombre, usu_apellido, usu_cargo, juz_id_usu) values ('esteban', '15-12-2019', '$2a$10$hEnhf4Izc3ids1mnQcmd/O6gsJG7WMs8LSCd8J7PmU7/bs1HNGqsO', true, 'Esteban', 'Salcedo Álvarez', 'Escribiente', 2);
insert into usuario (usu_username, usu_create_at, usu_password, usu_enabled, usu_nombre, usu_apellido, usu_cargo, juz_id_usu) values ('admin', '13-12-2019', '$2a$10$aAJMf3apCrhKeK04wo2LHuMAXWGXMzc95y.DZkQPYFfp1XXikKDIC', true, 'Paula Alejandra', 'Lozano Suarez', 'Jueza', 2);
insert into usuario (usu_username, usu_create_at, usu_password, usu_enabled, usu_nombre, usu_apellido, usu_cargo, juz_id_usu) values ('alejandra', '13-12-2019', '$2a$10$aAJMf3apCrhKeK04wo2LHuMAXWGXMzc95y.DZkQPYFfp1XXikKDIC', true, 'Jazmin ', 'Carrascal Bermúdez', 'Servidor', 2);
insert into usuario (usu_username, usu_create_at, usu_password, usu_enabled, usu_nombre, usu_apellido, usu_cargo, juz_id_usu) values ('super_admin', '22-05-2020', '$2a$10$Dm2BcCYxx0J2SpJb9fU7ee5DH4Z1dY5GJXCw4ecfDmlimc/dSAhti', true, 'Roberto', 'Salcedo Álvarez', 'Super Admin', 2);

insert into rol (rol_nombre, usu_id_rol) values ('ROLE_USER', 1);
insert into rol (rol_nombre, usu_id_rol) values ('ROLE_USER', 2);
insert into rol (rol_nombre, usu_id_rol) values ('ROLE_ADMIN', 2);
insert into rol (rol_nombre, usu_id_rol) values ('ROLE_USER', 4);
insert into rol (rol_nombre, usu_id_rol) values ('ROLE_ADMIN', 4);
insert into rol (rol_nombre, usu_id_rol) values ('ROLE_SUPER_ADMIN', 4);

insert into proceso_usuario (pro_id_pro_usu, usu_id_pro_usu) values (1, 2);
insert into proceso_usuario (pro_id_pro_usu, usu_id_pro_usu) values (2, 2);
insert into proceso_usuario (pro_id_pro_usu, usu_id_pro_usu) values (3, 2);
