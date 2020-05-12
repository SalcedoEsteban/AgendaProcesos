insert into especialidad (esp_nombre) values ('Familia');
insert into especialidad (esp_nombre) values ('Civil');
insert into especialidad (esp_nombre) values ('Penal');
insert into especialidad (esp_nombre) values ('Laboral');

insert into juzgado (juz_nombre, esp_id_juz) values ('Juzgado pimero de familia', 1);
insert into juzgado (juz_nombre, esp_id_juz) values ('Juzgado segundo de familia', 1);
insert into juzgado (juz_nombre, esp_id_juz) values ('Juzgado primero civil', 2);
insert into juzgado (juz_nombre, esp_id_juz) values ('Juzgado segundo civil', 2);



insert into tipo_proceso (tip_pro_nombre) values ('INV. PATERNIDAD');
insert into tipo_proceso (tip_pro_nombre) values ('ALIMENTOS');
insert into tipo_proceso (tip_pro_nombre) values ('IMP. PATERNIDAD');
insert into tipo_proceso (tip_pro_nombre) values ('CUSTODIA');




insert into proceso ( pro_radicado, pro_demandante, pro_demandado, pro_fecha_reparto, pro_ultima_actuacion, pro_estado_actual, pro_estado, pro_tipo_proceso, tip_pro_id_pro, pro_juzgado, juz_id_pro) values ( '2019-00200-01', ' Esteban Salcedo Alvarez', 'Alejandra Lozano', '02-03-2019', 'ADMISION', 'ESPERA DE NOT PER', true, 1, 1, 1, 1);
insert into proceso ( pro_radicado, pro_demandante, pro_demandado, pro_fecha_reparto, pro_ultima_actuacion, pro_estado_actual, pro_estado, pro_tipo_proceso, tip_pro_id_pro, pro_juzgado, juz_id_pro) values ( '2018-00300-02', ' Angel Roberto', 'Julian Castillo', '04-03-2019', 'ADMISION', 'ESPERA DE NOT PER', true, 2, 2, 1, 1);
insert into proceso ( pro_radicado, pro_demandante, pro_demandado, pro_fecha_reparto, pro_ultima_actuacion, pro_estado_actual, pro_estado, pro_tipo_proceso, tip_pro_id_pro, pro_juzgado, juz_id_pro) values ( '2018-00300-03', ' Angel Roberto', 'Julian Castillo', '04-03-2019', 'ADMISION', 'ESPERA DE NOT PER', true, 2, 2, 2, 2);



insert into termino (ter_nombre, ter_numero_dias, ter_basico, tip_pro_id_ter, esp_id_ter) values ('Perdida competencia', 365, true, 1, 1);
insert into termino (ter_nombre, ter_numero_dias, ter_basico, tip_pro_id_ter, esp_id_ter) values ('admision', 30, true, 1, 1);
insert into termino (ter_nombre, ter_numero_dias, ter_basico, tip_pro_id_ter, esp_id_ter) values ('indamision', 15, true, 1, 2);
insert into termino (ter_nombre, ter_numero_dias, ter_basico, tip_pro_id_ter, esp_id_ter) values ('admision', 15, true, 2, 3);
insert into termino (ter_nombre, ter_numero_dias, ter_basico, tip_pro_id_ter, esp_id_ter) values ('subsanar demanda', 15, true, 2, 3);
insert into termino (ter_nombre, ter_numero_dias, ter_basico, tip_pro_id_ter, esp_id_ter) values ('adminion', 15, true, 3, 3);



insert into detalle_termino (det_ter_dias_habiles, det_ter_fecha_incial, det_ter_fecha_final, ter_id_det_ter, pro_id_det_ter) values (true, '02-03-2019', '02-03-2020', 1, 1);
insert into detalle_termino (det_ter_dias_habiles, det_ter_fecha_incial, det_ter_fecha_final, ter_id_det_ter, pro_id_det_ter) values (true, '02-03-2019', '02-03-2020', 2, 1);
insert into detalle_termino (det_ter_dias_habiles, det_ter_fecha_incial, det_ter_fecha_final, ter_id_det_ter, pro_id_det_ter) values (true, '02-03-2019', '02-03-2020', 3, 1);
insert into detalle_termino (det_ter_dias_habiles, det_ter_fecha_incial, det_ter_fecha_final, ter_id_det_ter, pro_id_det_ter) values (true, '02-03-2019', '02-03-2020', 1, 2);
insert into detalle_termino (det_ter_dias_habiles, det_ter_fecha_incial, det_ter_fecha_final, ter_id_det_ter, pro_id_det_ter) values (true, '02-03-2019', '02-03-2020', 2, 2);


insert into usuario (usu_username, usu_password, usu_enabled, usu_nombre, juz_id_usu) values ('esteban', '$2a$10$H.1m8ANlFM3y.BojAvJ8oe9KA52MF.FCuaDKKE3F9O2YEs.XZ2qlO', true, 'Esteban Salcedo Alvarez', 1);
insert into usuario (usu_username, usu_password, usu_enabled, usu_nombre, juz_id_usu) values ('admin', '$2a$10$aAJMf3apCrhKeK04wo2LHuMAXWGXMzc95y.DZkQPYFfp1XXikKDIC', true, 'Paula Alejandra Lozano Suarez', 2);

insert into rol (rol_nombre, usu_id_rol) values ('ROLE_USER', 1);
insert into rol (rol_nombre, usu_id_rol) values ('ROLE_USER', 2);
insert into rol (rol_nombre, usu_id_rol) values ('ROLE_ADMIN', 2);

insert into proceso_usuario (pro_id_pro_usu, usu_id_pro_usu) values (1, 2);
insert into proceso_usuario (pro_id_pro_usu, usu_id_pro_usu) values (2, 2);
insert into proceso_usuario (pro_id_pro_usu, usu_id_pro_usu) values (3, 2);
