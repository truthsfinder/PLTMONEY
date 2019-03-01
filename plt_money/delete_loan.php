<?php
 	
 	require "db_config.php";

 	//delete expense
	$loan_id = $_GET['loan_id'];

	$query = "DELETE from loan WHERE loan_id = $loan_id";
	
	if(mysqli_query($con, $query)){
		echo "success";
	}else{
		echo "failed";
	}

?>