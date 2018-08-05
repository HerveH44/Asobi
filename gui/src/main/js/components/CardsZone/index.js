import React, {Fragment} from "react"
import {connect} from 'react-redux';
import {array, bool, func, number, object, string} from "prop-types"
import MainZone from "./MainZone";
import PackZone from "./PackZone";
import SideZone from "./SideZone";
import ZonesAsColumns from "./ZonesAsColumns";

const CardsZone = ({columnView}) => (
    columnView ?
        <div>
            <PackZone/>
            <ZonesAsColumns />
        </div>
        :
            <Fragment>
                <PackZone/>
                <MainZone/>
                <SideZone/>
            </Fragment>
);

CardsZone.propTypes = {
    columnView: bool.isRequired,
};

const mapStateToProps = ({gameSettings}) => ({
    columnView: gameSettings.columnView
});
const mapDispatchToProps = {
};

export default connect(mapStateToProps, mapDispatchToProps)(CardsZone);
