<?php
 	
 	require "db_config.php";

 	if($_SERVER['REQUEST_METHOD']=='GET'){

 		$user_id = $_GET['user_id'];
	 
	 	$query = "SELECT * FROM `budget` 
	 		WHERE `user_id` = $user_id AND
	 		budget_category = 'Weekly' AND
			budget_status = 'active'";
	 
	 	$res = mysqli_query($con,$query);
	 	
		$result = array();
		
		while($row = mysqli_fetch_array($res)){
			array_push($result,
				array('budget_id'=>$row[0],
				'user_id'=>$row[1],
				'budget_amount'=>$row[2],
				'budget_category'=>$row[3],
				'budget_status'=>$row[4],
				"status"=>'success'
			));
		}
		 
		echo json_encode(array("result"=>$result));
		 
		mysqli_close($con);
	 
	 }
?>
