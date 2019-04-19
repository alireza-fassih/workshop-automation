import React from 'react';
import { Col, Row, Button, Card, CardBody } from 'reactstrap';
import AbstractFormCreator from './AbstractFormCreator';


export const PillBtn = {
    borderRadius: "50em"
}

const LtrDir = {
    direction: 'ltr'
}

class SearchPanel extends AbstractFormCreator {

    constructor(props) {
        super(props);
        this.search = this.search.bind(this);
        this.xslReport = this.xslReport.bind(this);
    }

    componentDidMount() {
        this.search();
    }

    render() {
        let elements = this.props.items.map(item => {
            if (item.type === "date") {
                return this.createDateInput(12, 3, "search" , item);
            } else if ( item.type === "combo" ) {
                return this.createComboBox(12, 3, "search"  , item );
            } else {
                return this.createTextInput(12, 3, "search" , item);
            }
        });

        return (
            <Card>
                <CardBody>
                    <Row>
                        <Col xs="12">
                            <Row>{elements}</Row>
                            <Row style={LtrDir}>
                                <Col xs="12" md="2" hidden={!this.props.hasXslExport}>
                                    <Button type="submit" style={PillBtn} size="md"
                                        onClick={this.xslReport}>خروجی اکسل</Button>
                                </Col>
                                <Col xs="12" md="2">
                                    <Button type="submit" style={PillBtn} color="success" size="md" onClick={this.search}><i className="fa fa-search" aria-hidden="true"></i> جستجو</Button>
                                </Col>
                            </Row>
                        </Col>
                    </Row>
                </CardBody>
            </Card>
        )
    }

    search() {
        this.props.doSearch(this.refineProps());
    }

    xslReport() {
        this.props.generateXsl(this.refineProps());
    }

}

export default SearchPanel;