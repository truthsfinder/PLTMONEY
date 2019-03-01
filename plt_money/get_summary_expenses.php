<?php
 	
 	require "db_config.php";

 	if($_SERVER['REQUEST_METHOD']=='GET'){

 		$budget_category = $_GET['budget_category'];
 		$category = "";

 		if($budget_category == 0){
 			$category = "Daily";
 		}else if($budget_category == 1){
 			$category = "Weekly";
 		}else{
 			$category = "Monthly";
 		}
	 	
	 	$query = "SELECT expense.expense_id, expense.budget_id, expense.category_name, expense.expense_date, expense.expense_amount FROM `expense` 
				LEFT JOIN budget ON expense.budget_id = budget.budget_id
				WHERE budget.budget_category = '$category' AND budget.budget_status = 'active'";
	 
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
