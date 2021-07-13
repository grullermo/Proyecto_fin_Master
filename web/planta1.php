<?php 

	$conexion=mysqli_connect('IP BBDD', 'user', 'Password', 'myProyectoAgro', 'Port');

 ?>


<!DOCTYPE html>
<html>
<head>
	<title>mostrar datos</title>
</head>
<body>	
 <?php $varPlanta = $_GET["Nplanta"]; ?>
<?php	
	
	
	$sql="select P.nombre, P.cod_planta, P.variedad, P.fecha_siembra, D.nombre, L.nombre, M.mac from  planta P, Lugares L, propietario D, datos_planta M where P.cod_planta = $varPlanta and M.cod_planta = P.cod_planta and M.fecha_baja is null";
	 $result=mysqli_query($conexion,$sql);
	$mostrar=mysqli_fetch_array($result, MYSQLI_NUM)
		
?>	
<table width="200" border="1">
  <tbody>
    <tr>
      <td>&nbsp;Nombre</td>
      <td><label for="textfield"></label>
      <input name="NtextNombre" type="text" disabled="disabled" id="textNombre" value="<?php echo $mostrar[0] ?>"></td>
      <td><p>&nbsp;Codigo planta:</p>
      <p>&nbsp;</p></td>
      <td><label for="textfield"> </label>
      <input name="textfield" type="text" disabled="disabled" id="textfield" value="<?php echo $mostrar[1] ?>"></td>
    </tr>
    <tr>
      <td>&nbsp;Especie</td>
      <td><label for="textfield2"> </label>
      <input name="NtextEspecie" type="text" disabled="disabled" id="textEspecie" value="<?php echo $mostrar[2] ?>"></td>
      <td>&nbsp;Propietario:</td>
      <td><label for="textfield2"> </label>
      <input name="textfield2" type="text" disabled="disabled" id="textfield2" value="<?php echo $mostrar[4] ?>"></td>
    </tr>
    <tr>
      <td>&nbsp;Fecha de siembra</td>
      <td><label for="textfield"> </label>
      <input name="NtextfhSiembra" type="text" disabled="disabled" id="textfhSiembra" value="<?php echo $mostrar[3] ?>"></td>
     
	 <td>&nbsp;Municipio Campo:</td>
      <td><label for="textfield3"> </label>
      <input name="textfield3" type="text" disabled="disabled" id="textfield3" value="<?php echo $mostrar[5] ?> "></td>
    </tr>
  </tbody>
</table>
<p>
  <label for="textfield4">MAC del sensor de la Planta:</label>
  <input name="textfield4" type="text" disabled="disabled" id="textfield4" value="<?php echo $mostrar[6] ?>">	
  <?php 
		 $varMac = $mostrar[6];
		 mysqli_free_result($result);
	 ?>
  </p>
</p>
    <table border="1" >
		<tr>
			<td>Nombre</td>
			<td>Temperatura</td>
			<td>Humedad</td>
			<td>Luz</td>
			<td>Conductividad</td>
			<td>Bateria</td>
			<td>Fecha</td>
			<td>Mac</td>
		</tr>

		<?php 
		$sql="SELECT * from telemetria where mac = '$varMac' ";
		$result=mysqli_query($conexion,$sql);
		
		/* array asociativo */

        //printf ("%s (%s)\n", $row["Name"], $row["CountryCode"]);

		while($mostrar=mysqli_fetch_array($result, MYSQLI_ASSOC)){
		 ?>

		<tr>
			<td><?php echo $mostrar['nombre'] ?></td>
			<td><?php echo $mostrar['temperatura'] ?></td>
			<td><?php echo $mostrar['humedad'] ?></td>
			<td><?php echo $mostrar['luz'] ?></td>
			<td><?php echo $mostrar['conductividad'] ?></td>
			<td><?php echo $mostrar['bateria'] ?></td>
			<td><?php echo $mostrar['fecha'] ?></td>
			<td><?php echo $mostrar['mac'] ?></td>
		</tr>
	<?php 
	}
		 mysqli_free_result($result);
	 ?>
	</table>

</body>
</html>
