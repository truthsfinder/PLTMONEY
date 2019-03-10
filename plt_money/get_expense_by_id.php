<?php
 	
 	require "db_config.php";

 	if($_SERVER['REQUEST_METHOD']=='GET'){

	 	$expense_id = $_GET["expense_id"];

		$query = "SELECT * FROM expense WHERE expense_id = $expense_id";

		$res = mysqli_query($con, $query);

		$row = mysqli_fetch_array($res);

		if($row != NULL){
			$result = array();
			array_push($result,array(
			 "expense_id"=>$row['expense_id'],
			 "expense_name"=>$row['expense_name'],
			 "budget_id"=>$row['budget_id'],
			 "category_name"=>$row['category_name'],
			 "expense_date"=>$row['expense_date'],
			 "expense_amount"=>$row['expense_amount'],
			 "status"=>'success'
			 )
			);
			 
			echo json_encode(array("result"=>$result));
		}else{
			$result = array();
			array_push($result,array(
			 "expense_id" => 0,
			 "expense_name" => '',
			 "budget_id" => 0,
			 "category_name" => '',
			 "expense_date" =>'',
			 "expense_amount" => 0,
			 "status"=>'success'
			 )
			);
			 
			echo json_encode(array("result"=>$result));
		}
	}
?>
