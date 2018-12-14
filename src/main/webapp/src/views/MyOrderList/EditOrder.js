import React from 'react';
import { Card, CardBody, Col, Row, Button, Table, Input,
    Modal ,ModalHeader, ModalBody, ModalFooter, FormGroup } from 'reactstrap';
import AbstractFormCreator from '../Core/AbstractFormCreator';
import RestProvider from '../Core/RestProvider';


const listStyle = {
    marginTop: '20px'
};

export default class EditOrder extends AbstractFormCreator {

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
        this.rest.getCustom("loadOrder/"+this.props.match.params.id)
            .then(resp => {
                resp.data.metadata.forEach( meta => {         
                    let obj = {};
                    let options = {};
                    meta.metadata.forEach(item => {
                        obj["item" + item.id] = "";
                        options["item" + item.id] = item.values;
                    });
                    obj["metadata"] = meta.metadata;
                    obj["goodsTitle"] = meta.title;
                    obj["itemId"] = meta.id;
                    obj["options"] = options;
                    
                    resp.data.order.items.forEach( orderItem => {
                        if( orderItem.goodId === meta.id ) {
                            obj["count"] = orderItem.count;
                        }
                    })
                    let allGoods = this.state.goods;
                    allGoods.push(obj)
                    this.setState({ goods : allGoods });
                });
                this.setState({"description": resp.data.order.description});
                resp.data.order.items.forEach( orderItem => {
                    orderItem.items.forEach( innerItem => {
                        if( innerItem.selectable ) {
                            let obj = {};
                            obj["item" + innerItem.metadataId] = innerItem.materialId;
                            this.setState( obj );
                        }
                    })
                })
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
        
        this.rest.postCustom("editOrder/" + this.props.match.params.id , { 
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
            let tableBody = [];
            let totlaPrice = 0;
            this.state.order.items.forEach( order => {
                tableBody.push(  <tr><th>{order.goodsTitle} {order.count} عدد</th><th></th></tr> );
                tableBody.push( ... order.items.map( item => {
                    let itemTitle = item.title + ( item.selectable ? ": " + item.rawMaterialTitle : "");
                    return ( <tr><td>{itemTitle}</td><td>{item.price}</td></tr> )
                }));
                let price = 0;
                order.items.forEach( item => price += item.price );
                totlaPrice += price * order.count;
            })
            tableBody.push(  <tr><th>جمع کل</th><th>{totlaPrice}</th></tr> );
            return(
                <Modal isOpen={true} size="lg"  >
                    <ModalHeader>تایید نهایی سفارش</ModalHeader>
                    <ModalBody>
                        <Table responsive style={ { marginTop : '35px' } }>
                            <thead><tr><th>کالا</th><th>قیمت‌</th></tr></thead>
                            <tbody>{tableBody}</tbody>
                        </Table>
                    </ModalBody>
                    <ModalFooter>
                        <FormGroup>
                            <Button type="submit" size="sm" color="primary" onClick={this.dismiss.bind(this)}>بازگشت</Button>
                            <Button type="submit" size="sm" color="primary" onClick={this.save.bind(this)}>ثبت سفارش</Button>
                        </FormGroup>
                    </ModalFooter>
                </Modal>);
        } else {
            return undefined;
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