class UploadAdapter {
    constructor(loader, blogNm, header, token) {
        this.loader = loader;
        this.blogNm = blogNm;
        this.header = header;
        this.token = token;
    }

    upload() {
        return this.loader.file.then( file => new Promise(((resolve, reject) => {
            this._initRequest();
            this._initListeners( resolve, reject, file );
            this._sendRequest( file );
        })))
    }

    _initRequest() {
        const xhr = this.xhr = new XMLHttpRequest();
        xhr.open('POST', 'http://localhost:8080/posts/upload/'+this.blogNm, true);
        xhr.setRequestHeader(this.header, this.token);
        xhr.responseType = 'json';
    }

    _initListeners(resolve, reject, file) {
        const xhr = this.xhr;
        const loader = this.loader;
        const genericErrorText = '파일을 업로드 할 수 없습니다.'

        xhr.addEventListener('error', () => {reject(genericErrorText)})
        xhr.addEventListener('abort', () => reject())
        xhr.addEventListener('load', () => {
            const response = xhr.response
            if(!response || response.error) {
                return reject( response && response.error ? response.error.message : genericErrorText );
            }
            console.log("responseUrl: "+response.url);
            resolve({
                default: response.url, //업로드된 파일 주소
                oriImgName: response.oriImgName,
                tempUrl: response.tempUrl,
                imgName: response.imgName,
            })
        })
    }

    _sendRequest(file) {
        const data = new FormData()
        data.append('upload',file)
        this.xhr.send(data)
    }
}