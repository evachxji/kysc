import {Component, Inject, Input, OnInit, ViewChild, TemplateRef, ElementRef} from '@angular/core';
declare var $: any;
declare var Cropper: any;
declare var cropper: any;
@Component({
  selector: 'app-upload-image',
  templateUrl: './upload-image.component.html',
  styleUrls: ['./upload-image.component.css']
})
export class UploadImageComponent implements OnInit {
  @ViewChild('imageview') imge: ElementRef;
  @ViewChild('sel') sele: ElementRef;
  @ViewChild('file') fil: ElementRef;

  constructor() {
  }

  ngOnInit() {
  }

  selClick() {
    $(this.fil.nativeElement).click();
  }

  fileChange() {
    console.log($(this.fil.nativeElement)[0]);
    var objUrl = $(this.fil.nativeElement)[0].files[0];
    var windowURL = window.URL;
    var dataURL = windowURL.createObjectURL(objUrl);
    $(this.imge.nativeElement).attr("src", dataURL);
    var image1 =document.getElementById("imageview");
    console.log(image1);
    var cropper1 = new Cropper(image1, {
      aspectRatio: 12 / 12,
      crop: function (e) {
        console.log(e.detail.x);
        console.log(e.detail.y);
        console.log(e.detail.width);
        console.log(e.detail.height);
        console.log(e.detail.rotate);
        console.log(e.detail.scaleX);
        console.log(e.detail.scaleY);
        //$(this.imge.nativeElement).css("width","600px");
      }
    });
    //$(this.imge.nativeElement).Cropper('cropper1');
    cropper1.crop();
  }
  
}


