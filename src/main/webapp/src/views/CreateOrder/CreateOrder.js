import React from 'react';
import { Card, CardBody, Col, Row, Button, Table, Input,
    Modal ,ModalHeader, ModalBody, ModalFooter, FormGroup } from 'reactstrap';
import AbstractFormCreator from '../Core/AbstractFormCreator';
import RestProvider from '../Core/RestProvider';
import OrderDialog from '../OrderDialog/OrderDialog';


const listStyle = {
    marginTop: '20px'
};

export default class CreateOrder extends AbstractFormCreator {

    constructor(props) {
        super(props);
        this.state = { goods : [] , orders: []};
        this.createElements = this.createElements.bind(this);
        this.createElementsMetadata = this.createElementsMetadata.bind(this);
        this.refineSuggestData = this.refineSuggestData.bind( this );
        this.rest = new RestProvider("createOrder");
        this.confirmDialog = this.confirmDialog.bind( this );
        this.createOrderEntity = this.createOrderEntity.bind( this );
    }

    componentDidMount() {

        this.props.match.params.id.split("-").map(item => {
            let o = item.split("X");
            return { id: parseInt(o[1]), count: parseInt(o[0]) };
        }).forEach( elm => {
            this.rest.load(elm.id)
                .then(resp => {
                    let obj = {};
                    let options = {};
                    resp.data.metadata.forEach(item => {
                        obj["item" + item.id] = "";
                        options["item" + item.id] = item.values;
                    });
                    obj["metadata"] = resp.data.metadata;
                    obj["goodsTitle"] = resp.data.title;
                    obj["itemId"] = elm.id;
                    obj["options"] = options;
                    obj["count"] = elm.count;
                    let allGoods = this.state.goods;
                    allGoods.push(obj)
                    this.setState({ goods : allGoods });

                });
            });
        
    }

    createSuggest(id, lable, values) {
        return { id: id, type: "combo", label: lable,  values: values,
            convertToVal: it => it.id , convertToStr: it => it.title 
        };
    }

    fetchWidget(suggestType, categoryId, id, value) {
        if (value.reason === 'input-changed') {
            this.rest.getBase(suggestType + "/search", { page: 0, pageSize: 20, 'LIKE:title': value.value, "EQ:category.id": categoryId })
                .then(resp => {
                    let obj = {};
                    obj["suggest" + id] = resp.data.content;
                    this.setState(obj);
                });
        }
    }


    refineSuggestData( id, entity ) {
        let  elId = entity[ id ];
		if( elId ) {
			entity[id] = {
				id: elId
			};
		} else {
            entity[id] = undefined;
        }
    }

    clearWidget() {
        // do nothing
    }

    createElementsMetadata(metadata) {
        return metadata
            .map(element => this.createSuggest("item" + element.id, element.title, element.values));
    }

    createElements( element ) {
        return this.createElementsMetadata( element.metadata )
            .map(element => this.createComboBox(12, 3, "eidt", element));
    }

    createOrderEntity(element) {
        let metaData = this.createElementsMetadata(element.metadata);
        let entity = this.refineInternal( metaData );
        metaData.forEach( item => this.refineSuggestData( item.id, entity ) )
        let data = [];
        element.metadata.forEach( item => {
            data.push( { metadata: { id: item.id }  , material: { id: entity[ "item" + item.id].id } } );
        });
        return data;
    }

    calculatePrice() {
        let elements = this.state.goods.map( element => { 
            return {
                goods: { id : element.itemId },
                count: element.count,
                items: this.createOrderEntity(element)   
            }
        });
          
        this.rest.postCustom("calculatePrice", { 
            items: elements ,
            description: this.state.description 
        } ).then( resp => {
                this.setState( { order : resp.data } ) 
            });
        
    }


    save() {
        let elements = this.state.goods.map( element => { 
            return {
                goods: { id : element.itemId },
                count: element.count,
                items: this.createOrderEntity(element)   
            }
        });
        
        this.rest.postCustom("submitOrder", { 
            items: elements,
            description: this.state.description  
        }).then( resp => {
                this.props.history.push('/my-orders/')
            });
    }

    dismiss() {
        this.setState({ order : null })
    }

    confirmDialog() {
        if( this.state.order ) {
            return <OrderDialog 
                order={this.state.order} 
                title="تایید نهایی سفارش" 
                register={this.save.bind(this)} 
                dismiss={this.dismiss.bind(this)} />;
        }
    }

    render() {
        let cards = this.state.goods.map( element => {
            let elements = this.createElements( element );
            return (            
                <Card>
                    <CardBody>
                        <Row>فرم سفارشی سازی {element.goodsTitle}</Row>
                        <Row style={listStyle}>{elements}</Row>
                    </CardBody>
                </Card>
            );
        })
        return (
            <div className="animated fadeIn">
                <Row>
                    {this.confirmDialog()}
                    <Col xs="12">
                        {cards}

                        <Row>
                            <Col md="8" xs="12">
                                توضیحات
                                <Input type="textarea" name="description" id="orderDescription" 
                                    value={this.state.description} 
                                    id="orderDescription" 
                                    onChange={ ev => this.setState({ description: ev.target.value }) } />         
                            </Col>
                        </Row>
                        <Row>
                            <Button type="submit" size="sm" color="primary" onClick={this.calculatePrice.bind(this)}>
                                <i className="fa fa-plus" aria-hidden="true"></i> محاسبه قیمت
                            </Button>
                        </Row>
                    </Col>
                </Row>
            </div>);
    }


}