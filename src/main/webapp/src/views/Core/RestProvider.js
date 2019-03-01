import Axios from 'axios';

class RestProvider {


    constructor(prefix) {
        this.baseUrl = "/rest/";
        this.prefix = prefix;
        this.url = this.baseUrl + prefix;
    }

 
    getXsl(query) {
        return Axios.get(this.url + "/reportExcelReport", { params: query, withCredentials: true, responseType: 'blob' })
            .then(response => {
                let url = window.URL.createObjectURL(new Blob([response.data]));
                let link = document.createElement('a');
                link.href = url;
                link.setAttribute('download', this.prefix + '.csv');
                document.body.appendChild(link);
                link.click();
            });
    }

    load(id) {
        return Axios.get(this.url + "/" + id, { withCredentials: true })
            .then(resp => this.checkRedirection(resp));
    }
 
    delete(id) {
        return Axios.delete(this.url + "/" + id, { withCredentials: true })
            .then(resp => this.checkRedirection(resp));
    }

    restore(id) {
        return Axios.post(this.url + "/restore/" + id, {}, { withCredentials: true })
            .then(resp => this.checkRedirection(resp));
    }

    doSearch(query) {
        return Axios.get(this.url + "/search", { params: query, withCredentials: true })
            .then(resp => this.checkRedirection(resp));
    }

    save(entity) {
        return Axios.post(this.url, entity)
            .then(resp => this.checkRedirection(resp));
    }

    update(entity) {
        return Axios.put(this.url + "/" + entity.id, entity, { withCredentials: true })
            .then(resp => this.checkRedirection(resp));
    }

    postCustom(url, data) {
        return Axios.post(this.url + "/" + url, data, { withCredentials: true })
            .then(resp => this.checkRedirection(resp));
    }

    patchCustom(url, data) {
        return Axios.patch(this.url + "/" + url, data, { withCredentials: true })
            .then(resp => this.checkRedirection(resp));
    }

    getCustom(url, data) {
        return Axios.get(this.url + "/" + url, { params: data, withCredentials: true })
            .then(resp => this.checkRedirection(resp));
    }

    options() {
        return Axios.get(this.url + "/options", { withCredentials: true })
            .then(resp => this.checkRedirection(resp));
    }

    getBase(url, params) {
        return Axios.get(this.baseUrl + url, { params: params, withCredentials: true })
            .then(resp => this.checkRedirection(resp));
    }


    checkRedirection(resp) {
        if ((resp.status / 100) !== 2 && resp.data && typeof resp.data === 'string' && resp.data.length > 5) {
            window.location.assign("/login")
        } else {
            return resp;
        }
    }
}

export default RestProvider;