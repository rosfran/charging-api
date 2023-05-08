export const solarGridColumns = [
  { field: "id", headerName: "Id", width: 100 },
  {
    field: "name",
    headerName: "Solar Grid Name",
    width: 400,
    valueGetter: ({ row }) => row.name,
  },
  {
    field: "age",
    headerName: "Age",
    width: 400,
    valueGetter: ({ row }) => row.age,
  },
  {
    field: "powerOutput",
    headerName: "Power Output",
    width: 400,
    valueGetter: ({ row }) => row.powerOutput,
  },
];
