<?php
 	
 	require "db_config.php";

 	if($_SERVER['REQUEST_METHOD']=='GET'){

 		$budget_id = $_GET['budget_id'];
	 
	 	$query = "SELECT * FROM `expense` 
	 		WHERE `budget_id` = $budget_id";
	 
	 	$res = mysqli_query($con,$query);
	 	
		$result = array();
		
		while($row = mysqli_fetch_array($res)){
			array_push($result,
				array('expense_id'=>$row[0],
				'budget_id'=>$row[1],
				'category_name'=>$row[2],
				'expense_date'=>$row[3],
				'expense_amount'=>$row[4],
				"status"=>'success'
			));
		}
		 
		echo json_encode(array("result"=>$result));
		 
		mysqli_close($con);
	 
	 }
?>
