import React from "react";
import PropTypes from "prop-types";

import uniqueId from "lodash/uniqueId";

const NewsPanel = ({motd}) => (
    <fieldset className='fieldset'>
        <legend className='legend'>News</legend>
        {motd.map(x => <li key={uniqueId()}>{x}</li>)}
    </fieldset>
);

NewsPanel.propTypes = {
    motd: PropTypes.array
};

export default NewsPanel;
