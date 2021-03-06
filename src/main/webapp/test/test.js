var pageNo = 1,
pageSize = 6,
fulledtimes,
templateSrcBtn = $('#time-button-template').html(),
templateSrcRoom = $('#room-template').html(),
id
;
var trGeneratorBtn = Handlebars.compile(templateSrcBtn);
var trGeneratorRoom = Handlebars.compile(templateSrcRoom);


$(document).on('DOMContentLoaded', function (e) { 
    var calendarEl = document.getElementById('calendar');
    window.calendar = new FullCalendar.Calendar(calendarEl, {
	plugins : [ 'dayGrid', 'interaction'],
	locale: 'ko',
	selectable: true,
	dateClick: function(info) {
	    window.calInfo = info;
	    $(document.body).trigger('cal-dateClick');
	},
	eventClick: function(info) {
	    window.eventInfo = info.event;
	    $(document.body).trigger('cal-eventClick');
	},
	events: '/studyboot/app/json/schedule/list'
    });


    window.calendar.render();
});


$("#email-btn").click((e) => {
    $.getJSON('/studyboot/app/json/mail/send?email='+ $("#email").val(), function(obj) {
	id = obj.id;
    }
    );
});

$("#id-btn").click((e) => {
    if($("#id").val() == id){
	alert("일치합니다!");
    }
});




//모달이 출력되기전에 캘린더를 렌더하면 안되기때문에(짤림) 모달이 shown된 뒤 캘린더를 렌더(출력)한다.
$('#calModal').on('shown.bs.modal', function (e) {
    $('#time-btn').html('');
    $('#roomList').html('');
    $(".cal-time").each(function(index, time){
	$(time).prop('disabled', false);
    })
    window.calendar.render();
    $(document.body).trigger('loaded-cal');
});

//캘린더의 날짜를 누르면 예약불가능한 시간(예약이 다 찬 시간)은 disabled 한다.
$(document.body).bind('cal-dateClick', () => {
    $('#roomList').html('');
    $('#time-btn').html('');
    $(trGeneratorBtn()).appendTo($('#time-btn'));

    $(".cal-time").each(function(index, time){
	$(time).prop('disabled', false);
    })

    $.getJSON('../app/json/spaceroombooking/allbookedtime?spaceNo='
	    + 101 + "&date=" + window.calInfo.dateStr, function(obj) {
		fulledtimes = obj.fulledtimes;
		$(".cal-time").each(function(index, time){
		    $(fulledtimes).each(function(index2, fulledtime){
			if($(time).text() == fulledtime)
			    $(time).prop('disabled', true);
		    })
		});
		$(document.body).trigger('loaded-calbtn');
	    });
});

//시간버튼을 누르면 해당 시간에 예약가능한 방의 리스트를 출력한다.
$(document.body).bind('loaded-calbtn', () => {
    $(".cal-time").click((e) => {
	$('#roomList').html('');
	$.getJSON('../app/json/spaceroom/listroom?spaceNo='
		+ 101 + "&date=" + window.calInfo.dateStr +" " + $(e.target).text(), function(obj) {
		    $(trGeneratorRoom(obj)).appendTo($('#roomList'));
		}
	);
    });
});



$(document.body).bind('cal-eventClick', () => {
    alert(window.eventInfo.title + window.eventInfo.id);
});



//$("#calendar").fullCalendar({

//dayClick: function() {
//},
//weekends: false,
//showNonCurrentDates: false,
//locale: 'pt-br',
//render: true
//});

//$(".ui .full-calendar").addClass('modal');




