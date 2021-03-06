var param = location.href.split('?')[1],
nossss = param.split('=')[1],
fileTemplateSrc = $('#amazonfile-add').html();

var fileGenerator = Handlebars.compile(fileTemplateSrc);

$(document).ready(function() {
  // 게시판으로 진입했을 때 탭 색깔 활성화
  $(document.body).bind('loaded-nav', () => {
    $('#std-repository').removeClass('list-group-item list-group-item-action justify-content-between');
    $('#std-repository').addClass('list-group-item justify-content-between active minee');
    $('.minee').attr('style', "cursor: pointer; color: white;");
  });

  // 위쪽 빵 부스러기 적용할 때 기존껄 span 태그에서 리스트 태그로 바꿔서 적용시키기
  var $mystudy = $("<a class='u-link-v5 g-color-main g-color-primary--hover' " +
      "href='/studyboot/html/mystudy/index.html?no=" + nossss + "'>"
      + "마이 스터디" + "</a><i class='fa fa-angle-right g-ml-7'></i>");
  $('#std-main').html($mystudy);

  // study board 빵부스러기 생성하기
  if($('#std-main2').val() == undefined){
    var $stdBoard = $("<li class='list-inline-item g-color-primary' id='std-main2'>"
        + "<span>자료실</span></li>");
    $('#conts-list').append($stdBoard);
  }
});


function loadFileList(nossss) {
  $.getJSON('../../app/json/MyStudy/fileList?stdNo=' + nossss,
      function(obj){
    
    console.log(obj);

    // 핸들바스를 통해서 가져온 리스트 업핸드 해주기
    $(fileGenerator(obj)).appendTo('#amazon-file');
    $(document.body).trigger('loaded-repository');
  });
};
loadFileList(nossss);


$('#add-file').change(function() {

  var fileName = $('#add-file').val().replace(/.*(\/|\\)/, '');
  console.log(fileName);
  $('#file-upload').append("<p>" + fileName);
})

$('#add-file').fileupload({
  url: '../../app/json/MyStudy/fileAdd',        // 서버에 요청할 URL
  dataType: 'json',         // 서버가 보낸 응답이 JSON임을 지정하기
  sequentialUploads: true,
  singleFileUploads: false,
  autoUpload: false,
  replaceFileInput : true,
  add : function (e, data) {
    $('#save-btn').click(function() {
      data.submit();
    });
  },
  formData : {studyNo: nossss},
  done: function (e, data) {
    Swal.fire({
      type: 'success',
      title: '업로드가 완료되었습니다.',
      showConfirmButton: false,
      timer: 1500
    }).then((result) => {
      location.reload();
    })
  }
});


$(document.body).bind('loaded-repository', () => {
  
  $.HSCore.components.HSDropdown.init($('[data-dropdown-target]'), {dropdownHideOnScroll: false});
  
  // 파일을 삭제하는 이벤트
  $('.delete-files').click((e) => {
    var fileName = $(e.target).attr('data-content')

    $.getJSON('../../app/json/MyStudy/fileDelete?fileName=' + fileName + "&studyNo=" + nossss,
        function(obj){
      Swal.fire({
        type: 'success',
        title: '삭제가 완료되었습니다.',
        showConfirmButton: false,
        timer: 1500
      }).then((result) => {
        location.reload();
      })
    });
  });

  // 파일을 다운로드하는 이벤트
  $('.file-Download').click((e) => {
    // 아직까지 핸들바스로 받아온 파일이 없고 받아온 파일에 해당 파일의 이름을 받을 수 있는 조건이 없어서 임시로 fileName 설정
    var fileName = $('.file-Download').attr('data-content');
    location.href = '../../app/json/FileDown/fileDownload?fileName=' + fileName + "&studyNo=" + nossss;
  });

});






















