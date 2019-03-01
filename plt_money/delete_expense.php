<?php
 	
 	require "db_config.php";

 	//delete expense
	$expense_id = $_GET['expense_id'];

	$query = "DELETE from expense WHERE expense_id = $expense_id";
	
	if(mysqli_query($con, $query)){
		echo "success";
	}else{
		echo "failed";
	}

?>