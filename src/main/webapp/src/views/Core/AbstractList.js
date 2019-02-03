import React, { Component } from 'react';
import SearchPanel, {PillBtn} from './SearchPanel';
import {
    Card, CardBody, Col, Row,
    Table, Button, Pagination, PaginationItem, PaginationLink
} from 'reactstrap';
import RestProvider from './RestProvider';
import CreateEntity from './CreateEntity';


const listStyle = {
    marginTop: '20px'
};

const ButtonStyle = {
    marginLeft: "10px"
}

class AbstractList extends Component {

    constructor(props) {
        super(props);
        this.state = {
            page: {
                number: 0,
                size: 10,
                content: []
            },
            createEntity: false,
            data: undefined
        }
        this.rest = new RestProvider(this.getEntityName());
        this.doSearch = this.doSearch.bind(this);
        this.openSeachDialog = this.openSeachDialog.bind(this);
        this.disMissDialog = this.disMissDialog.bind(this);
        this.createPagination = this.createPagination.bind(this);
        this.saveEntity = this.saveEntity.bind(this);
        this.renderPagination = this.renderPagination.bind(this);
        this.fetchPage = this.fetchPage.bind(this);
        this.createPanelInternal = this.createPanelInternal.bind(this);
        this.createSuggest = this.createSuggest.bind(this);
        this.refineSuggestData = this.refineSuggestData.bind(this);
        this.createAble = this.createAble.bind(this);
        this.refineComboBox = this.refineComboBox.bind(this);
        this.createCombo = this.createCombo.bind( this );
        this.createEditIcon = this.createEditIcon.bind(this);
        this.createIcon = this.createIcon.bind(this);
        this.createDeleteIcon = this.createDeleteIcon.bind(this);
        this.createRestoreIcon = this.createRestoreIcon.bind(this);
        this.createControleIcons = this.createControleIcons.bind(this);
        this.elipseString = this.elipseString.bind(this);
        this.hasXslExport = this.hasXslExport.bind(this);
        this.generateXsl = this.generateXsl.bind(this);
    }

    refineComboBox( id , entity ) {
        let  elId = entity[ id ];
    		if( elId ) {
    			entity[id] = {
    				id: elId
    			};
    		} else {
                entity[id] = undefined;
    		}
    }

    componentDidMount() {
        this.rest.options()
            .then(resp => this.setState({ options: resp.data }));
    }

    openSeachDialog() {
        this.setState({ createEntity: true });
    }

    disMissDialog() {
        this.setState({ createEntity: false, data: undefined });
    }

    deleteEntity(id) {
        this.rest.delete(id)
            .then( resp => this.doSearch() );
    }

    restoreEntity(id) {
        this.rest.restore(id)
            .then( resp => this.doSearch() );
    }

    createEditIcon(data) {
        return this.createIcon("fa fa-pencil", this.editEntity.bind(this, data.id));
    }

    createDeleteIcon(data) {
        return this.createIcon("fa fa-times", this.deleteEntity.bind(this, data.id), data.deleted);
    }

    createRestoreIcon(data) {
        return this.createIcon("fa fa-recycle", this.restoreEntity.bind(this, data.id), !data.deleted);
    }

    createControleIcons( data ) {
        return [
            this.createEditIcon(data),
            this.createDeleteIcon(data),
            this.createRestoreIcon(data)
        ];
    }

    elipseString( str, maxSize ) {
        return (str && str.length > maxSize) ? str.substring(0,maxSize) + '...' : str;
    }

    createIcon(className, onClick, hidden) {
        return (<a style={ButtonStyle} hidden={hidden} href="javascript:void(0);"><i className={className} aria-hidden="true" onClick={onClick}></i></a>);
    }

    doSearch(query) {
        if (query !== undefined) {
            this.lastQuery = query;
        } else {
            query = this.lastQuery || {};
        }
        query.page = this.state.page.number;
        query.pageSize = this.state.page.size;
        this.rest.doSearch(query)
            .then(resp => this.setState({ page: resp.data }));
    }

    saveEntity(entity) {
        if (this.state.data === undefined) {
            return this.rest.save(entity)
                .then(resp => this.disMissDialog())
                .then(() => this.doSearch());;
        } else {
            let options = this.getCreatePanel();
            let newDate = this.state.data;
            options.forEach(item => {
                newDate[item.id] = entity[item.id];
            });
            return this.rest.update(newDate)
                .then(resp => this.disMissDialog())
                .then(() => this.doSearch());
        }
    }

    createPagination() {
        let pages = [];
        if (!this.state.page.first) {
            pages.push(this.renderPagination((this.state.page.number + 1) - 1));
        }
        pages.push(this.renderPagination((this.state.page.number + 1)));
        if (!this.state.page.last) {
            pages.push(this.renderPagination((this.state.page.number + 1) + 1));
        }
        return pages;
    }

    fetchPage(num) {
        if ((this.state.page.number + 1) !== num) {
            let p = this.state.page;
            p.number = num - 1;
            this.setState({ page: p });
            this.doSearch();
        }
    }

    renderPagination(num) {
        return (
            <PaginationItem key={"page_" + num}>
                <PaginationLink onClick={() => this.fetchPage(num)}
                    active={((this.state.page.number + 1) === num).toString()} tag="button">{num}</PaginationLink>
            </PaginationItem>
        );
    }

    createPanelInternal() {
        let options = this.getCreatePanel();
        if (this.state.data !== undefined) {
            options.forEach(item => {
                if (item.value === undefined) {
                    item.value = this.state.data[item.id];
                }
            });
        }
        return options;
    }

    refineSuggestData(id, suggestType, entity) {
        let all = [];
        if (this.state.data && this.state.data[id]) {
            all.push(this.state.data[id]);
        }
        if (this.state[suggestType]) {
            all = all.concat(this.state[suggestType]);
        }
        let val = all.filter(item => item.title === entity[id])[0];
        entity[id] = val ? { id: val.id } : undefined;
    }

    editEntity(id) {
        this.rest.load(id)
            .then(resp => this.setState({ data: resp.data, createEntity: true }));
    }

    createSuggest(id, lable, placeholder, suggestType) {
        let value = (this.state.data && this.state.data[id] ? this.state.data[id].title : '');
        let emptyArray = (value !== '' ? [this.state.data[id]] : []);
        return {
            id: id, type: 'suggest', label: lable, value: value,
            values: this.state[suggestType] === undefined ? emptyArray : this.state[suggestType],
            fetch: this.fetchWidget.bind(this, suggestType), clear: this.clearWidget.bind(this),
            convertVal: val => val.title, renderSuggestion: item => <span>{item.title}</span>,
            placeholder: placeholder
        };
    }

    createCombo( id, lable, options) {
        let value = ( this.state.data && this.state.data[id] ? this.state.data[id].id : undefined );
        return { id: id, type: "combo", label: lable, value: value, values: this.state.options ? this.state.options[options] : [],
            convertToVal: it => it.id , convertToStr: it => it.title
        };
    }

    fetchWidget(suggestType, value) {
        if (value.reason === 'input-changed') {
            this.rest.getBase(suggestType + "/search", { page: 0, pageSize: 20, 'LIKE:title': value.value })
                .then(resp => {
                    let obj = {};
                    obj[suggestType] = resp.data.content;
                    this.setState(obj);
                });
        }
    }

    clearWidget() {
        // do nothing
    }

    createAble() {
        return true;
    }

    generateXsl(param) {
        this.rest.getXsl(param);
    }

    hasXslExport() {
        return false;
    }


    render() {
        let search = this.getSearchPanel();
        let tableHead = this.getTableHead().map(item => <th key={"head_" + item}>{item}</th>)
        let tableBody = this.state.page.content.map(item => this.convertToTableRow(item));
        let hasPanel = this.getCreatePanel !== undefined;
        let panelOptions = {};
        if (hasPanel) {
            panelOptions = this.createPanelInternal();
        }
        let pagination = this.createPagination();

        return (
            <div className="animated fadeIn">
                <SearchPanel items={search} doSearch={this.doSearch} hasXslExport={this.hasXslExport()} generateXsl={this.generateXsl} />
                {hasPanel && this.state.createEntity &&
                    <CreateEntity items={panelOptions} onSave={this.saveEntity} show={this.state.createEntity}
                        dismiss={this.disMissDialog}  />}
                {this.renderCustomElement !== undefined && this.renderCustomElement()}
                <Row style={listStyle}>
                    <Col xs="12">
                        <Card>
                            <CardBody >
                                <Row>
                                    {this.createAble() &&
                                        <Col xs="12" md="2">
                                            <Button type="submit" style={PillBtn} size="md" color="primary" onClick={this.openSeachDialog}>
                                                <i className="fa fa-plus" aria-hidden="true"></i> ایجاد
                                            </Button>
                                        </Col>
                                    }
                                </Row>
                                <Row style={listStyle}>
                                    <Table  striped  responsive>
                                        <thead><tr>{tableHead}</tr></thead>
                                        <tbody>{tableBody}</tbody>
                                    </Table>
                                </Row>
                                <Row style={listStyle}>
                                    <Pagination>
                                        <PaginationItem onClick={() => this.fetchPage(this.state.page.number)} hidden={this.state.page.first}>
                                            <PaginationLink  previous tag="button"></PaginationLink>
                                        </PaginationItem>
                                        {pagination}
                                        <PaginationItem onClick={() => this.fetchPage(this.state.page.number + 2)} hidden={this.state.page.last}>
                                            <PaginationLink next tag="button"></PaginationLink>
                                        </PaginationItem>
                                    </Pagination>
                                </Row>
                            </CardBody>
                        </Card>
                    </Col>
                </Row>
            </div>
        )
    }

}


export default AbstractList;
