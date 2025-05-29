-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3307
-- Tiempo de generación: 29-05-2025 a las 19:16:46
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `almacen_db`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `entradas`
--

CREATE TABLE `entradas` (
  `id` int(11) NOT NULL,
  `producto_id` int(11) DEFAULT NULL,
  `cantidad` int(11) NOT NULL,
  `fecha_ingreso` date DEFAULT curdate(),
  `proveedor` varchar(100) DEFAULT NULL,
  `usuario_id` int(11) DEFAULT NULL,
  `numero_factura` varchar(50) DEFAULT current_timestamp(),
  `observaciones` text DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `entradas`
--

INSERT INTO `entradas` (`id`, `producto_id`, `cantidad`, `fecha_ingreso`, `proveedor`, `usuario_id`, `numero_factura`, `observaciones`) VALUES
(9, 3, 5, '2025-05-25', 'Sony Import', 1, 'FACT-1747414278010', ''),
(21, 3, 4, '2025-05-15', 'Sony Import', 1, 'FACT-1747769215490', ''),
(22, 1, 2, '2025-05-01', 'HP Distribuidores', 1, 'FACT-1747841540675', ''),
(28, 48, 10, '2025-05-22', 'Papelerías Vélez', 1, 'FAC000028', ''),
(29, 4, 5, '2025-05-08', 'Tech Supplies', 1, 'FACT-1747934634970', ''),
(30, 6, 6, '2025-05-23', 'Dell Express', 1, 'FACT-1747934640815', ''),
(31, 15, 11, '2025-05-22', 'Tecnología Doméstica', 1, 'FACT-1747934646987', ''),
(32, 10, 23, '2025-05-22', 'Seguridad Total', 1, 'FACT-1747934656264', ''),
(33, 1, 3, '2025-05-22', 'HP Distribuidores', 1, 'FACT-1747947621988', ''),
(35, 5, 7, '2025-02-03', 'Logitech Distribuidores', 1, 'FACT-1747948159410', ''),
(39, 1, 10, '2025-05-23', 'HP Distribuidores', 1, 'FACT-1748021080581', 'Entrada por pedido'),
(41, 1, 3, '2025-05-26', 'HP Distribuidores', 1, 'FACT-1748277231143', ''),
(43, 1, 5, '2025-05-26', 'HP Distribuidores', 1, 'FACT-1748286187702', ''),
(44, 2, 3, '2024-09-12', 'Samsung Store', 1, 'FACT-1748286369334', ''),
(45, 13, 2, '2025-05-02', 'Hogar y Café', 1, 'FACT-1748286708965', ''),
(46, 3, 2, '2024-11-03', 'Sony Import', 1, 'FACT-1748286724752', ''),
(47, 55, 43, '2025-05-27', 's', 1, 'FAC000047', ''),
(48, 56, 10, '2025-05-27', 'Inditex', 1, 'FAC000048', ''),
(49, 11, 3, '2024-02-07', 'Tech Storage', 1, 'FACT-1748374010271', ''),
(50, 17, 4, '2025-05-08', 'Ferretería Express', 1, 'FACT-1748377052050', ''),
(51, 12, 5, '2025-05-06', 'Memorias Store', 1, 'FACT-1748444396509', 'Pedido por bajo Stock'),
(52, 20, 4, '2025-05-07', 'Bolsos & Maletas', 4, 'FACT-1748444796378', 'Pedido por bajo Stock'),
(53, 19, 5, '2025-05-08', 'Apple Store', 4, 'FACT-1748444813836', ''),
(54, 4, 5, '2025-05-07', 'Tech Supplies', 4, 'FACT-1748444826122', ''),
(55, 2, 3, '2025-05-08', 'Samsung Store', 4, 'FACT-1748444845011', ''),
(56, 6, 5, '2025-05-08', 'Dell Express', 4, 'FACT-1748444856015', ''),
(57, 7, 7, '2025-05-09', 'Epson Store', 4, 'FACT-1748444864693', ''),
(58, 9, 10, '2025-05-09', 'Muebles Finos', 4, 'FACT-1748444880223', 'Pedido por bajo Stock'),
(59, 12, 4, '2025-05-12', 'Memorias Store', 4, 'FACT-1748444902773', ''),
(60, 13, 5, '2025-05-12', 'Hogar y Café', 4, 'FACT-1748444914294', ''),
(61, 14, 7, '2025-05-12', 'Hogar Electro', 4, 'FACT-1748444925292', ''),
(62, 16, 5, '2025-05-12', 'Ferretería Global', 4, 'FACT-1748444938969', ''),
(63, 17, 8, '2025-05-12', 'Ferretería Express', 4, 'FACT-1748444953989', ''),
(64, 18, 7, '2025-05-13', 'Deportes Center', 4, 'FACT-1748444966586', ''),
(65, 19, 6, '2025-05-13', 'Apple Store', 4, 'FACT-1748444975565', ''),
(66, 20, 4, '2025-05-12', 'Bolsos & Maletas', 4, 'FACT-1748444983906', ''),
(67, 48, 4, '2025-05-13', 'Papelerías Vélez', 4, 'FACT-1748444989358', ''),
(68, 55, 7, '2025-05-13', 'Muebles al momento', 4, 'FACT-1748444994809', ''),
(69, 56, 6, '2025-05-12', 'Inditex', 4, 'FACT-1748445001492', ''),
(70, 57, 10, '2025-05-28', 'Segurisur', 4, 'FAC000070', '');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `codigo` varchar(50) NOT NULL,
  `categoria` varchar(50) DEFAULT NULL,
  `descripcion` text DEFAULT NULL,
  `cantidad` int(11) DEFAULT 0,
  `proveedor` varchar(100) DEFAULT NULL,
  `fecha_ingreso` date DEFAULT NULL,
  `marca` varchar(50) DEFAULT NULL,
  `stock_minimo` int(11) DEFAULT 0,
  `imagen` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id`, `nombre`, `codigo`, `categoria`, `descripcion`, `cantidad`, `proveedor`, `fecha_ingreso`, `marca`, `stock_minimo`, `imagen`) VALUES
(1, 'Portátil HP Pavilion', 'P001', 'Electrónica', 'Portátil con procesador Intel i5 y 8GB RAM', 1, 'HP Distribuidores', '2024-04-23', 'HP', 2, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\portatilhp.png'),
(2, 'Smartphone Samsung Galaxy', 'P002', 'Electrónica', 'Teléfono móvil 128GB, 8GB RAM', 10, 'Samsung Store', '2024-10-31', 'Samsung', 3, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\samsung.png'),
(3, 'Auriculares Bluetooth Sony', 'P003', 'Electrónica', 'Auriculares inalámbricos con cancelación de ruido', 5, 'Sony Import', '2024-04-02', 'Sony', 5, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\auriculares.png'),
(4, 'Teclado Mecánico RGB', 'P004', 'Accesorios', 'Teclado mecánico para gaming con retroiluminación RGB', 16, 'Tech Supplies', '2024-04-01', 'HyperX', 5, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\teclado.png'),
(5, 'Mouse Gamer Logitech', 'P005', 'Accesorios', 'Mouse óptico con sensor de alta precisión', 17, 'Logitech Distribuidores', '2024-03-31', 'Logitech', 4, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\raton.png'),
(6, 'Monitor 27\" Dell', 'P006', 'Electrónica', 'Monitor LED 144Hz Full HD', 21, 'Dell Express', '2024-03-30', 'Dell', 3, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\monitor.png'),
(7, 'Impresora Multifunción Epson', 'P007', 'Oficina', 'Impresora, escáner y copiadora todo en uno', 1, 'Epson Store', '2024-03-29', 'Epson', 2, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\impresora.png'),
(9, 'Escritorio de Madera', 'P009', 'Muebles', 'Escritorio moderno con almacenamiento', 11, 'Muebles Finos', '2024-03-27', 'WoodLine', 2, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\escritorio.png'),
(10, 'Cámara de Seguridad WiFi', 'P010', 'Seguridad', 'Cámara con detección de movimiento y visión nocturna', 47, 'Seguridad Total', '2024-03-26', 'Xiaomi', 3, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\camara.png'),
(11, 'Disco Duro Externo 1TB', 'P011', 'Almacenamiento', 'Disco duro portátil USB 3.0', 25, 'Tech Storage', '2024-03-25', 'Seagate', 5, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\discoduro.png'),
(12, 'Memoria USB 64GB', 'P012', 'Almacenamiento', 'USB 3.1 de alta velocidad', 17, 'Memorias Store', '2024-03-24', 'SanDisk', 10, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\usb.png'),
(13, 'Cafetera Automática', 'P013', 'Electrodomésticos', 'Cafetera con molinillo integrado', 8, 'Hogar y Café', '2024-03-23', 'Nespresso', 2, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\cafetera.png'),
(14, 'Microondas Digital', 'P014', 'Electrodomésticos', 'Microondas con pantalla táctil y grill', 17, 'Hogar Electro', '2024-03-22', 'LG', 2, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\microondas.png'),
(15, 'Aspiradora Robot', 'P015', 'Electrodomésticos', 'Aspiradora inteligente con WiFi', 28, 'Tecnología Doméstica', '2024-03-21', 'Roomba', 1, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\aspiradora.png'),
(16, 'Taladro Eléctrico Bosch', 'P016', 'Herramientas', 'Taladro inalámbrico con batería de litio', 17, 'Ferretería Global', '2024-03-20', 'Bosch', 3, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\taladro.png'),
(17, 'Set de Destornilladores', 'P017', 'Herramientas', 'Juego de destornilladores de precisión', 17, 'Ferretería Express', '2024-03-19', 'Stanley', 5, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\destornilladores.png'),
(18, 'Zapatillas Deportivas Nike', 'P018', 'Ropa y Calzado', 'Zapatillas para correr con amortiguación', 38, 'Deportes Center', '2024-03-18', 'Nike', 5, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\zapatillas.png'),
(19, 'Reloj Inteligente Apple Watch', 'P019', 'Accesorios', 'Reloj con monitoreo de salud y notificaciones', 28, 'Apple Store', '2024-03-17', 'Apple', 2, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\relojapple.png'),
(20, 'Mochila para Laptop', 'P020', 'Accesorios', 'Mochila con compartimentos acolchados', 9, 'Bolsos & Maletas', '2024-03-16', 'Samsonite', 5, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\mochila.png'),
(48, 'Paquete de Folios A4', 'P046', 'Oficina', 'Paquete de 500 folios A4', 14, 'Papelerías Vélez', '2025-05-22', 'Navigator', 5, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\folios.png'),
(55, 'Silla', 'P050', 'Muebles', 'Silla Gaming para oficina', 50, 'Muebles al momento', '2025-05-27', 'XRacer', 3, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\imagenes\\sillagaming.png'),
(56, 'Vestido de Lunares', 'P056', 'Ropa y Calzado', 'Vestido de lunares largo', 3, 'Inditex', '2025-05-27', 'Zara', 2, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\src\\assets\\vestido.png'),
(57, 'Alarma para el hogar', 'P057', 'Seguridad', 'Kit de slarma de seguridad con control wifi protección inteligente para tu hogar', 1, 'Segurisur', '2025-05-28', 'Ajax', 5, 'C:\\Users\\fraan\\git\\Gestion-Almacen\\imagenes\\alarma.png');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `salidas`
--

CREATE TABLE `salidas` (
  `id` int(11) NOT NULL,
  `producto_id` int(11) DEFAULT NULL,
  `departamento` varchar(100) DEFAULT NULL,
  `cantidad` int(11) NOT NULL,
  `fecha_salida` date DEFAULT curdate(),
  `motivo` text DEFAULT NULL,
  `usuario_id` int(11) DEFAULT NULL,
  `numeroFactura` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `salidas`
--

INSERT INTO `salidas` (`id`, `producto_id`, `departamento`, `cantidad`, `fecha_salida`, `motivo`, `usuario_id`, `numeroFactura`) VALUES
(12, 11, 'Mantenimiento', 5, '2025-05-01', 'Reparación', 1, 'FAC-20250520213806'),
(13, 5, 'Mantenimiento', 4, '2025-05-15', 'Reparación', 1, 'FAC-20250520214026'),
(14, 18, 'Mantenimiento', 4, '2025-05-21', 'Reparación', 1, 'FAC-20250521191307'),
(15, 1, 'Producción', 8, '2025-05-21', 'Ventas', 1, 'FAC-20250521191812'),
(16, 2, 'Oficinas', 9, '2025-05-21', 'Ventas', 1, 'FAC-20250521191933'),
(17, 3, 'Logística', 53, '2025-05-21', 'Cambio de almacén', 1, 'FAC-20250521192223'),
(18, 4, 'Oficinas', 26, '2025-05-21', 'Repuesto de Oficina', 1, 'FAC-20250521192530'),
(19, 5, 'Oficinas', 10, '2025-05-21', 'Repuesto en Oficinas', 1, 'FAC-20250521193932'),
(20, 12, 'Logística', 25, '2025-05-21', 'Venta', 1, 'FAC-20250521194350'),
(21, 10, 'Mantenimiento', 14, '2025-05-21', 'Reparación', 1, 'FAC-20250521194635'),
(22, 13, 'Producción', 6, '2025-05-01', 'Ventas', 1, 'FAC-20250521195534'),
(23, 9, 'Logística', 9, '2025-05-10', 'Cambio de almacén', 1, 'FAC-20250521200119'),
(24, 4, 'Oficinas', 4, '2025-05-23', 'Recambio de dispositivos', 1, 'FAC-20250523192930'),
(25, 1, 'Oficinas', 31, '2025-05-26', 'Los requiere oficina', 1, 'FAC-20250526201155'),
(26, 1, 'Mantenimiento', 1, '2024-05-16', 'Reparación', 1, 'FAC-20250526202502'),
(27, 20, 'Producción', 19, '2025-05-26', 'Venta', 1, 'FAC-20250526220343'),
(28, 1, 'Oficinas', 10, '2025-05-05', 'Utilización', 1, 'FAC-20250527204120'),
(29, 7, 'Mantenimiento', 13, '2025-05-26', 'Reparación', 4, 'FAC-20250528171418'),
(30, 7, 'Producción', 1, '2025-05-27', 'Venta', 4, 'FAC-20250528171504'),
(31, 57, 'Producción', 9, '2025-05-26', 'Venta', 5, 'FAC-20250528171815'),
(32, 56, 'Producción', 13, '2025-05-27', 'Venta', 5, 'FAC-20250528171838'),
(33, 17, 'Logística', 20, '2025-05-27', 'Ventas', 5, 'FAC-20250528172048');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuarios`
--

CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `usuario` varchar(50) NOT NULL,
  `contraseña` varchar(255) NOT NULL,
  `rol` varchar(50) DEFAULT NULL,
  `estado` enum('activo','inactivo') DEFAULT 'activo'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuarios`
--

INSERT INTO `usuarios` (`id`, `nombre`, `usuario`, `contraseña`, `rol`, `estado`) VALUES
(1, 'Francis', 'admin', '$2a$10$Wah3AXHO2Wwo/WfLWiEK3OBvW6sX1Z1g9.s.nKCLM62R3HbwuRo72', 'admin', 'activo'),
(2, 'francis diaz', 'francis11', '$2a$10$Qv4xnhq.fSXW/rkDsfxGGuNDNoP8OwgiMdS5IMU20HvebRYyVEVVa', 'usuario', 'activo'),
(3, 'Juan Pérez', 'juanp', '$2a$10$zRxoik4iLxhYzFLv9gXx9OYqCZhYQMQvMcFZBH0AHriGbRv9N6GtS', 'admin', 'activo'),
(4, 'Ana Gómez', 'anag', '$2a$10$Z/IPBvM8E.48LQSw2Py9meQeNd8PC521NC6uDR4Jdjgwb788e/KDa', 'usuario', 'activo'),
(5, 'Carlos Sánchez', 'carloss', '$2a$10$rGfI2/0kWTi41l2ETQL2Ye3WMJ2nFhyKPJDv0kurAC3UcaZeP0vMO', 'usuario', 'activo'),
(6, 'Luisa Martínez', 'luisam', '$2a$10$QdAD.ZxPdTIHCjo4BNJb9uy6KcDZZePaf2RHdK4BoCqa1RsZCgKAq', 'admin', 'inactivo'),
(7, 'Miguel Torres', 'miguelt', '$2a$10$/zMxvIDSCalm2Luc8t8Z8uQ85Qvzz5VxhkmnuXWhZOOtN9O17Mfu.', 'usuario', 'activo'),
(9, 'Pepe', 'pepito', '$2a$10$vh.AVt5oYpK5WWiMGkk1RemqBVKeaq2gtcPA.tVJ7GAV3FTqLZiz2', 'usuario', 'activo'),
(14, 'dani', 'daniel', '$2a$10$.VaV9gC8vGSgL/Nau2rD5.MPwGLbEmdNW6pfCjiKd4412cHUvTn9y', 'usuario', 'activo'),
(15, 'maria', 'mariaisa', '$2a$10$5BaGIQO7G35MF.CEG52PJOZHf6/ERuYg.MJiMpoQU1sLz1ZcVO/QO', 'usuario', 'activo');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `entradas`
--
ALTER TABLE `entradas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `producto_id` (`producto_id`),
  ADD KEY `usuario_id` (`usuario_id`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `codigo` (`codigo`);

--
-- Indices de la tabla `salidas`
--
ALTER TABLE `salidas`
  ADD PRIMARY KEY (`id`),
  ADD KEY `producto_id` (`producto_id`),
  ADD KEY `usuario_id` (`usuario_id`);

--
-- Indices de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `usuario` (`usuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `entradas`
--
ALTER TABLE `entradas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=71;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=58;

--
-- AUTO_INCREMENT de la tabla `salidas`
--
ALTER TABLE `salidas`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=34;

--
-- AUTO_INCREMENT de la tabla `usuarios`
--
ALTER TABLE `usuarios`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `entradas`
--
ALTER TABLE `entradas`
  ADD CONSTRAINT `entradas_ibfk_1` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `entradas_ibfk_2` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL;

--
-- Filtros para la tabla `salidas`
--
ALTER TABLE `salidas`
  ADD CONSTRAINT `salidas_ibfk_1` FOREIGN KEY (`producto_id`) REFERENCES `productos` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `salidas_ibfk_2` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
