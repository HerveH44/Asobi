import React from "react";
import PropTypes from "prop-types";

import _ from "Lib/utils";

const NewsPanel = ({motd}) => (
    <fieldset className='fieldset'>
        <legend className='legend'>News</legend>
        {motd.map(x => <li key={_.uid()}>{x}</li>)}
    </fieldset>
);

NewsPanel.propTypes = {
    motd: PropTypes.array
};

export default NewsPanel;
