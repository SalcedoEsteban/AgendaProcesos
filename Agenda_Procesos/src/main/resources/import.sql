insert into especialidad (esp_id, esp_nombre) values (1, 'Familia');
insert into especialidad (esp_id, esp_nombre) values (2, 'Civil');
insert into especialidad (esp_id, esp_nombre) values (3, 'Penal');
insert into especialidad (esp_id, esp_nombre) values (4, 'Laboral');

insert into juzgado (juz_id, juz_nombre, esp_id_juz) values (1, 'Juzgado pimero de familia', 1);
insert into juzgado (juz_id, juz_nombre, esp_id_juz) values (2, 'Juzgado segundo de familia', 1);
insert into juzgado (juz_id, juz_nombre, esp_id_juz) values (3, 'Juzgado primero civil', 2);
insert into juzgado (juz_id, juz_nombre, esp_id_juz) values (4, 'Juzgado segundo civil', 2);



insert into tipo_proceso (tip_pro_id, tip_pro_nombre) values (1, 'INV. PATERNIDAD');
insert into tipo_proceso (tip_pro_id, tip_pro_nombre) values (2, 'ALIMENTOS');
insert into tipo_proceso (tip_pro_id, tip_pro_nombre) values (3, 'IMP. PATERNIDAD');
insert into tipo_proceso (tip_pro_id, tip_pro_nombre) values (4, 'CUSTODIA');




insert into proceso (pro_id, pro_radicado, pro_demandante, pro_demandado, pro_fecha_reparto, pro_ultima_actuacion, pro_estado_actual, pro_estado, pro_tipo_proceso, tip_pro_id_pro, pro_juzgado, juz_id_pro) values (1, '2019-00200-01', ' Esteban Salcedo Alvarez', 'Alejandra Lozano', '02-03-2019', 'ADMISION', 'ESPERA DE NOT PER', 1, 1, 1, 1, 1);
insert into proceso (pro_id, pro_radicado, pro_demandante, pro_demandado, pro_fecha_reparto, pro_ultima_actuacion, pro_estado_actual, pro_estado, pro_tipo_proceso, tip_pro_id_pro, pro_juzgado, juz_id_pro) values (2, '2018-00300-02', ' Angel Roberto', 'Julian Castillo', '04-03-2019', 'ADMISION', 'ESPERA DE NOT PER', 0, 2, 2, 1, 1);
insert into proceso (pro_id, pro_radicado, pro_demandante, pro_demandado, pro_fecha_reparto, pro_ultima_actuacion, pro_estado_actual, pro_estado, pro_tipo_proceso, tip_pro_id_pro, pro_juzgado, juz_id_pro) values (3, '2018-00300-03', ' Angel Roberto', 'Julian Castillo', '04-03-2019', 'ADMISION', 'ESPERA DE NOT PER', 0, 2, 2, 2, 2);



insert into termino (ter_id, ter_nombre, ter_numero_dias, ter_basico, tip_pro_id_ter, esp_id_ter) values (1, 'Perdida competencia', 365, true, 1, 1);
insert into termino (ter_id, ter_nombre, ter_numero_dias, ter_basico, tip_pro_id_ter, esp_id_ter) values (2, 'admision', 30, true, 1, 1);
insert into termino (ter_id, ter_nombre, ter_numero_dias, ter_basico, tip_pro_id_ter, esp_id_ter) values (3, 'indamision', 15, true, 1, 2);



insert into detalle_termino (det_ter_id, det_ter_dias_habiles, det_ter_fecha_incial, det_ter_fecha_final, ter_id_det_ter, pro_id_det_ter) values (1, true, '02-03-2019', '02-03-2020', 1, 1);
insert into detalle_termino (det_ter_id, det_ter_dias_habiles, det_ter_fecha_incial, det_ter_fecha_final, ter_id_det_ter, pro_id_det_ter) values (2, true, '02-03-2019', '02-03-2020', 2, 1);
insert into detalle_termino (det_ter_id, det_ter_dias_habiles, det_ter_fecha_incial, det_ter_fecha_final, ter_id_det_ter, pro_id_det_ter) values (3, true, '02-03-2019', '02-03-2020', 3, 1);
insert into detalle_termino (det_ter_id, det_ter_dias_habiles, det_ter_fecha_incial, det_ter_fecha_final, ter_id_det_ter, pro_id_det_ter) values (4, true, '02-03-2019', '02-03-2020', 1, 2);
insert into detalle_termino (det_ter_id, det_ter_dias_habiles, det_ter_fecha_incial, det_ter_fecha_final, ter_id_det_ter, pro_id_det_ter) values (5, true, '02-03-2019', '02-03-2020', 2, 2);