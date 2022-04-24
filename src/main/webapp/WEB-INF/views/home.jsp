<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<html>
<head>
<title>Home</title>

<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />


<link rel="stylesheet" type="text/css"
	href="https://cdn.datatables.net/v/dt/dt-1.11.5/datatables.min.css" />
<script type="text/javascript" src="../../resources/js/jquery-3.6.0.min.js"></script>
<script type="text/javascript" src="../../resources/js/datatables.js"></script>

</head>
<body>
	<div class="">
		<div>
			<form method="POST" id="search-bar">
				<label for="f_name" class="label">이름</label> <input type="text"
					name="f_name" class="form-control" value=""> <label
					for="f_job" class="label">직업</label> <input type="text"
					name="f_job" class="input" value=""> <label
					for="f_location" class="label">위치</label> <input type="text"
					name="f_location" class="input" value=""> <label
					for="f_salary" class="label">임금</label> <input type="text"
					name="f_salary" class="input" value=""> <label for="f_date"
					class="label">날짜</label> <input type="text" name="f_date"
					class="input" value="">
			</form>
			<button class="btn-search">검색</button>
		</div>
		<table id="app" class="display" style="width: 100%">
			<thead>
				<tr>
					<th>이름</th>
					<th>성</th>
					<th>직업</th>
					<th>위치</th>
					<th>임금</th>
					<th>날짜</th>
				</tr>
			</thead>
		</table>
	</div>



	<script>
		$(document).ready(function() {
			var dataTable = $('#app').DataTable({
				"lengthChange" : false,
				"serverSide" : true,
				"paging" : true,
				"searching" : false,
				"pageLength" : 15,
				"info" : true,
				"columns" : [ {
					"data" : "first_name"
				}, {
					"data" : "last_name"
				}, {
					"data" : "job"
				}, {
					"data" : "location"
				}, {
					"data" : "salary"
				}, {
					"data" : "date"
				} ],
				"language" : {
					emptyTable : "검색 기록이 없습니다",
					info : " _START_ of _END_페이지  총 _TOTAL_건",
					paginate : {
						next : "다음",
						previous : "이전"
					}
				},
				"ajax" : {
					url : "/ajax/index",
					data : function(params) {
						params.param = $("#search-bar").serialize();
						console.log(params);
					},
					dataSrc : function(res) {
						console.log(res);
						var data = res.data;
						return data;
					}
				}

			});

			$(".btn-search").on("click", function(event) {

				dataTable.ajax.reload();
			})
		});
	</script>

</body>
</html>
