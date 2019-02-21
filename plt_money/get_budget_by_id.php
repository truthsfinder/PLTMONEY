<?php
 	
 	require "db_config.php";

 	$budget_id = $_GET['budget_id'];

	$query = "SELECT * FROM budget WHERE budget_id = $budget_id;";

	$res = mysqli_query($con, $query);

	$row = mysqli_fetch_array($res);

	if($row != NULL){
		$result = array();
		array_push($result,array(
		 "budget_id"=>$row['budget_id'],
		 "user_id"=>$row['user_id'],
		 "budget_amount"=>$row['budget_amount'],
		 "budget_category"=>$row['budget_category'],
		 "status"=>'success'
		 )
		);
		 
		echo json_encode(array("result"=>$result));
		 
		mysqli_close($con);
	}else{
		echo 'No available budget!';
	}
?>
