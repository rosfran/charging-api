import {
  Button,
  FormControl,
  Grid,
  InputLabel,
  MenuItem,
  Select,
  Stack,
  TextField,
} from "@mui/material";
import { useSnackbar } from "notistack";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../../components/navbar/Navbar";
import Sidebar from "../../components/sidebar/Sidebar";
import AuthService from "../../services/AuthService";
import HttpService from "../../services/HttpService";
import "./solargrid.scss";

const NewSolarGrid = () => {
  const pageTitle = "Upload New Solar Grid";
  const defaultValues = {
    name: "",
    typeId: "",
    userId: AuthService.getCurrentUser()?.id,
  };

  const { enqueueSnackbar } = useSnackbar();
  const navigate = useNavigate();
  const [formValues, setFormValues] = useState(defaultValues);
  const [solarGrids, setSolarGrids] = useState([]);

  useEffect(() => {
    const getSolarGrids = async () => {
      const response = await HttpService.getWithAuth("/users");
      const solarGrids = await response.data.content;
      setSolarGrids(solarGrids);
    };
    getSolarGrids();
  }, []);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormValues({
      ...formValues,
      [name]: value,
    });
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    HttpService.postWithAuth("/solargrid", formValues)
      .then((response) => {
        enqueueSnackbar("Solar Grid created successfully", { variant: "success" });
        navigate("/solargrid");
      })
      .catch((error) => {
        if (error.response?.data?.errors) {
          error.response?.data?.errors.map((e) =>
            enqueueSnackbar(e.field + " " + e.message, { variant: "error" })
          );
        } else if (error.response?.data?.message) {
          enqueueSnackbar(error.response?.data?.message, { variant: "error" });
        } else {
          enqueueSnackbar(error.message, { variant: "error" });
        }
      });
  };

  return (
    <div className="single">
      <Sidebar />
      <div className="singleContainer">
        <Navbar />
        <div className="bottom">
          <h1 className="title">{pageTitle}</h1>
          <form onSubmit={handleSubmit}>
            <Grid
              container
              alignItems="left"
              justify="center"
              direction="column"
              spacing={2}
            >
              <Grid item>
                <TextField
                  sx={{ width: 240 }}
                  autoFocus
                  required
                  id="name"
                  name="name"
                  label="Name"
                  solarGrid="text"
                  value={formValues.name}
                  onChange={handleInputChange}
                />
              </Grid>
              <Grid item>
                <FormControl sx={{ width: 240 }}>
                  <InputLabel id="demo-select-small">Type</InputLabel>
                  <Select
                    required
                    name="typeId"
                    label="Type"
                    value={formValues.typeId}
                    onChange={handleInputChange}
                  >
                    <MenuItem value="">
                      <em>------------ none ------------</em>
                    </MenuItem>
                    {solarGrids.map((solarGrid) => (
                      <MenuItem key={solarGrid.id} value={solarGrid.id}>
                        {solarGrid.name}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              </Grid>
            </Grid>
            <Stack spacing={2} sx={{ py: 3, paddingRight: 0 }} direction="row">
              <Button
                sx={{ minWidth: 112 }}
                variant="outlined"
                onClick={() => navigate("/solargrid")}
              >
                Cancel
              </Button>
              <Button sx={{ minWidth: 112 }} solarGrid="submit" variant="contained">
                Add
              </Button>
            </Stack>
          </form>
        </div>
      </div>
    </div>
  );
};

export default NewSolarGrid;
